package com.github.kurtloong.entrybuffer.scheduled;

import com.github.kurtloong.entrybuffer.util.RedisUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * @author kurt.loong
 * @version 1.0
 * @date 2021/5/26 16:38
 */
@Component
@Slf4j
public class TurnoverFlowScheduled {
    private final RedisUtil redisUtil;

    private static final String HOT_ACCOUNT_TURNOVER = "hot_account_turnover_flow";



    public TurnoverFlowScheduled(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }



    @Scheduled(cron = "0 0/1 * * * ?")
    public void process() throws InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        Set<ZSetOperations.TypedTuple<String>> sets = redisUtil.zRangeWithScores(HOT_ACCOUNT_TURNOVER,0,-1);
        List<ZSetOperations.TypedTuple<String>> typedTuples = Lists.newArrayList(sets);

        // 提交可分解的PrintTask任务
        forkJoinPool.submit(new TurnoverFlowTask(typedTuples));
        //阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
        forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);

        forkJoinPool.shutdown();
    }




}
