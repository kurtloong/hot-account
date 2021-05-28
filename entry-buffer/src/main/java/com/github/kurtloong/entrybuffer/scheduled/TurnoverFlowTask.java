package com.github.kurtloong.entrybuffer.scheduled;

import com.github.kurtloong.entrybuffer.domain.Turnover;
import com.github.kurtloong.entrybuffer.domain.TurnoverFlow;
import com.github.kurtloong.entrybuffer.mapper.BalanceMapper;
import com.github.kurtloong.entrybuffer.mapper.TurnoverFlowMapper;
import com.github.kurtloong.entrybuffer.mapper.TurnoverMapper;
import com.github.kurtloong.entrybuffer.util.RedisUtil;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import static com.github.kurtloong.entrybuffer.util.TableUtil.getTableName;

/**
 * 分治任务
 *
 * @author kurt.loong
 * @version 1.0
 * @date 2021 /5/26 19:36
 */
public class TurnoverFlowTask extends RecursiveAction {

    private static final long serialVersionUID = 2434451110880247717L;

    private static final int THRESHOLD = 10;

    private static final String HOT_ACCOUNT_TURNOVER = "hot_account_turnover_flow";

    private static final String VERSION = "_version";

    private static final BigDecimal MULTIPLE = new BigDecimal("100");



    @Resource
    private TurnoverFlowMapper flowMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private TurnoverMapper turnoverMapper;
    @Resource
    private BalanceMapper balanceMapper;

    private final List<ZSetOperations.TypedTuple<String>> sets;

    /**
     * Instantiates a new Turnover flow task.
     *
     * @param sets the sets
     */
    public TurnoverFlowTask(List<ZSetOperations.TypedTuple<String>> sets) {
        this.sets = sets;
    }


    /**
     * 把流水表中的流水 提交到明细表中 并且更新余额
     */
    @Override
    protected void compute() {
        int size = this.sets == null ? 0 : this.sets.size();

        if (CollectionUtils.isEmpty(sets)){
            return;
        }

        if (size < THRESHOLD) {
            //对每个账户进行汇总入账
            this.sets.forEach(stringTypedTuple -> {
                String accountId = stringTypedTuple.getValue();
                double timestamp = stringTypedTuple.getScore();
                List<TurnoverFlow> flowList = flowMapper.searchAllByAccountIdAndTurnoverFlowStatus(accountId,false);

                if (CollectionUtils.isEmpty(flowList)){
                    if (timestamp == redisUtil.zScore(HOT_ACCOUNT_TURNOVER,accountId)){
                        redisUtil.zRemove(HOT_ACCOUNT_TURNOVER,accountId);
                    }
                    return;
                }
                //处理每一条流水
                flowList.forEach(this::transaction);
                //如果缓存余额为负数 更新缓存余额
                if (redisUtil.get(accountId)!= null && Integer.parseInt(redisUtil.get(accountId))<0){
                    redisUtil.set(accountId,balanceMapper.getBalanceByAccountId(accountId, getTableName(accountId)).multiply(MULTIPLE).toPlainString());
                }
            });

        } else {
            //递归
            TurnoverFlowTask left = new TurnoverFlowTask(this.sets.subList(0, size / 2));
            left.fork();
            TurnoverFlowTask right = new TurnoverFlowTask(this.sets.subList(size / 2 + 1, size));
            right.fork();
        }
    }

    /**
     * 合成一个事务
     *
     * @param turnoverFlow the turnover flow
     */
    @Transactional(rollbackFor = Exception.class)
    public void transaction(TurnoverFlow turnoverFlow){
        Turnover turnover = new Turnover();
        turnover.setTurnoverUid(turnoverFlow.getAccountId());
        turnover.setAccountId(turnoverFlow.getAccountId());
        turnover.setAmount(turnoverFlow.getAmount());
        turnover.setTurnoverType(turnoverFlow.getTurnoverType());
        //获取版本号
        if (!redisUtil.hasKey(VERSION)){
            Integer version = turnoverMapper.selectTurnoverVersionByAccountId(turnoverFlow.getAccountId());
            redisUtil.set(turnoverFlow.getAccountId()+VERSION,version == null? "0":String.valueOf(version));
        }

        turnover.setTurnoverVersion(redisUtil.incrBy(turnoverFlow.getAccountId()+VERSION,1).intValue());

        BigDecimal beforeAmount = balanceMapper.getBalanceByAccountId(turnoverFlow.getAccountId(), getTableName(turnoverFlow.getAccountId()));
        BigDecimal afterAmount = turnoverFlow.getTurnoverType() == 0?beforeAmount.add(turnoverFlow.getAmount()):beforeAmount.subtract(turnoverFlow.getAmount());

        turnover.setBeforeBalance(beforeAmount);
        turnover.setAfterBalance(afterAmount);

        turnoverMapper.insertAll(turnover);
        balanceMapper.updateBalanceByAccount(turnover.getAccountId(), getTableName(turnover.getAccountId()),beforeAmount);
        turnoverFlow.setTurnoverFlowStatus(true);
        flowMapper.updateByPrimaryKey(turnoverFlow);


    }







}
