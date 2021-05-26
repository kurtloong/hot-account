package com.github.kurtloong.entrybuffer.core;

import java.math.BigDecimal;

/**
 * 这里是交易的策略，实际上针对不同的账户应该有不同的交易策略，这里只实现热点账户
 *
 * @author kurt.loong
 * @version 1.0
 * @date 2021 /5/26 11:14
 */
public abstract class TradeHandler {

    /**
     * 减扣过程
     *
     * @param turnoverUid 上游流水号
     * @param accountId   减扣的账户
     * @param amount      金额
     * @return the boolean
     */
    public abstract boolean deductionProcess(String turnoverUid, String accountId, BigDecimal amount);

    /**
     * 充值过程
     *
     * @param turnoverUid 上游流水号
     * @param accountId   充值账号
     * @param amount      金额
     * @return the boolean
     */
    public abstract boolean rechargeProcess(String turnoverUid ,String accountId, BigDecimal amount);

    /**
     * 转账过程
     *
     * @param turnoverUid      上游流水号
     * @param deductionAccount 减扣的账户
     * @param rechargeAccount  充值账号
     * @param amount           金额
     * @return the boolean
     */
    public abstract boolean transferAccountsProcess(String turnoverUid ,String deductionAccount, String rechargeAccount,BigDecimal amount);



}
