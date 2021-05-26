package com.github.kurtloong.entrybuffer.scheduled;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * @author kurt.loong
 * @version 1.0
 * @date 2021/5/26 19:36
 */
public class TurnoverFlowTask extends RecursiveAction {

    private static final long serialVersionUID = 2434451110880247717L;

    private static final int THRESHOLD = 10;

    private final List<ZSetOperations.TypedTuple<String>> sets;

    public TurnoverFlowTask(List<ZSetOperations.TypedTuple<String>> sets) {
        this.sets = sets;
    }


    /**
     * The main computation performed by this task.
     */
    @Override
    protected void compute() {
        int size = this.sets == null ? 0 : this.sets.size();

        if (size < THRESHOLD) {
            this.sets.forEach(stringTypedTuple -> {
                String accountId = stringTypedTuple.getValue();
                double timestamp = stringTypedTuple.getScore();

            });

        } else {
            TurnoverFlowTask left = new TurnoverFlowTask(this.sets.subList(0, size / 2));
            left.fork();
            TurnoverFlowTask right = new TurnoverFlowTask(this.sets.subList(size / 2 + 1, size));
            right.fork();
        }


    }
}
