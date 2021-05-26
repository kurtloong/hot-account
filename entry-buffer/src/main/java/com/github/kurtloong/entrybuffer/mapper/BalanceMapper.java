package com.github.kurtloong.entrybuffer.mapper;

import com.github.kurtloong.entrybuffer.domain.Balance;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * The interface Balance mapper.
 *
 *
 *
 */
@Mapper
public interface BalanceMapper {
    /**
     * 获取账户余额
     * 注意 该语句加上了共享读锁  lock in share mode
     * @param accountId 账户id
     * @return 余额
     */
    BigDecimal getBalanceByAccountId(String accountId);
}




