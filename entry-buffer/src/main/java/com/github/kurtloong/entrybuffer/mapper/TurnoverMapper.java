package com.github.kurtloong.entrybuffer.mapper;

import com.github.kurtloong.entrybuffer.domain.Turnover;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Turnover mapper.
 *
 * @Entity com.github.kurtloong.entrybuffer.domain.Turnover
 */
@Mapper
public interface TurnoverMapper {
    /**
     * Insert all.
     *
     * @param turnover the turnover
     */
    void insertAll(Turnover turnover);

    /**
     * 拿出最新的version id
     * 加上了 lock in share mode
     * @param accountId the account id
     * @return the integer
     */
    Integer selectTurnoverVersionByAccountId(String accountId);




}




