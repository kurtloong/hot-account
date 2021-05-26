package com.github.kurtloong.entrybuffer.core;

import com.github.kurtloong.entrybuffer.domain.Turnover;
import com.github.kurtloong.entrybuffer.domain.TurnoverFlow;
import com.github.kurtloong.entrybuffer.mapper.BalanceMapper;
import com.github.kurtloong.entrybuffer.mapper.TurnoverFlowMapper;
import com.github.kurtloong.entrybuffer.mapper.TurnoverMapper;
import com.github.kurtloong.entrybuffer.util.RedisUtil;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author kurt.loong
 * @version 1.0
 * @date 2021/5/26 11:28
 */
@Component
public class EntryBufferTradeHandler extends TradeHandler {

    private static final String HOT_ACCOUNT_TURNOVER = "hot_account_turnover_flow";

    private static final BigDecimal MULTIPLE = new BigDecimal("100");

    private final BalanceMapper balanceMapper;
    private final RedisUtil redisUtil;
    private final TurnoverFlowMapper turnoverFlowMapper;

    public EntryBufferTradeHandler(RedisUtil redisUtil, BalanceMapper balanceMapper, TurnoverFlowMapper turnoverFlowMapper) {
        this.redisUtil = redisUtil;
        this.balanceMapper = balanceMapper;
        this.turnoverFlowMapper = turnoverFlowMapper;
    }

    /**
     * 减扣过程
     *
     * @param turnoverUid 上游流水号
     * @param accountId   减扣的账户
     * @param amount      金额
     * @return the boolean
     */
    @Override
    public boolean deductionProcess(String turnoverUid, String accountId, BigDecimal amount) {
        Preconditions.checkNotNull(turnoverUid, "流水号不能为空");
        Preconditions.checkNotNull(accountId, "交易账户不能为空");
        Preconditions.checkNotNull(amount, "交易金额不能为空");
        //缓存交易流水
        redisUtil.zAdd(HOT_ACCOUNT_TURNOVER, accountId, System.currentTimeMillis());

        if (!redisUtil.hasKey(accountId)) {
            BigDecimal balance = balanceMapper.getBalanceByAccountId(accountId);
            redisUtil.set(accountId, balance.multiply(MULTIPLE).toPlainString());
        }

        redisUtil.incrBy(accountId,BigDecimal.ZERO.subtract(amount.multiply(MULTIPLE)).intValue());

        int result = Integer.parseInt(redisUtil.get(accountId));

        if (result<0) {
            return false;
        }

        TurnoverFlow turnoverFlow = new TurnoverFlow(turnoverUid, amount, accountId, 1);
        turnoverFlowMapper.insert(turnoverFlow);

        return true;


    }

    /**
     * 充值过程
     *
     * @param turnoverUid 上游流水号
     * @param accountId   充值账号
     * @param amount      金额
     * @return the boolean
     */
    @Override
    public boolean rechargeProcess(String turnoverUid, String accountId, BigDecimal amount) {
        //缓存交易流水
        redisUtil.zAdd(HOT_ACCOUNT_TURNOVER, accountId, System.currentTimeMillis());
        TurnoverFlow turnoverFlow = new TurnoverFlow(turnoverUid, amount, accountId, 0);
        turnoverFlowMapper.insert(turnoverFlow);
        return true;
    }

    /**
     * 转账过程
     *
     * @param turnoverUid      上游流水号
     * @param deductionAccount 减扣的账户
     * @param rechargeAccount  充值账号
     * @param amount           金额
     * @return the boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferAccountsProcess(String turnoverUid, String deductionAccount, String rechargeAccount, BigDecimal amount) {
       boolean deductionResult = deductionProcess(turnoverUid,deductionAccount,amount);
       boolean rechargeResult = rechargeProcess(turnoverUid,rechargeAccount,amount);

       if (!(deductionResult&&rechargeResult)){
           throw new RuntimeException();
       }

        return true;
    }


}
