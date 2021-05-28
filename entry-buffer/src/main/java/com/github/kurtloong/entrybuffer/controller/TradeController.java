package com.github.kurtloong.entrybuffer.controller;

import com.github.kurtloong.entrybuffer.core.EntryBufferTradeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * The type Trade controller.
 *
 * @author kurt.loong
 * @version 1.0
 * @date 2021 /5/27 16:32
 */
@RestController
@RequestMapping("/trade")
public class TradeController {
    /**
     * The Trade handler.
     */
    @Autowired
    EntryBufferTradeHandler tradeHandler;

    /**
     * 减扣.
     *
     * @param turnoverUid 上游流水号
     * @param accountId   减扣账号
     * @param amount      金额
     */
    @PostMapping("/deduction")
    public void  deduction(@RequestParam("turnoverUid") String turnoverUid,@RequestParam("accountId") String accountId, @RequestParam("amount")BigDecimal amount){
        tradeHandler.deductionProcess(turnoverUid,accountId,amount);
    }

    /**
     * 入账.
     *
     * @param turnoverUid 上游流水号
     * @param accountId   入账账号
     * @param amount      金额
     */
    @PostMapping("/rechargeProcess")
    public void  rechargeProcess(@RequestParam("turnoverUid") String turnoverUid,@RequestParam("accountId") String accountId, @RequestParam("amount")BigDecimal amount){
        tradeHandler.rechargeProcess(turnoverUid,accountId,amount);
    }

    /**
     * 转账
     *
     * @param turnoverUid      上游流水号
     * @param deductionAccount 减扣账号
     * @param rechargeAccount  入账账号
     * @param amount           金额
     */
    @PostMapping("/transferAccountsProcess")
    public void transferAccountsProcess(@RequestParam("turnoverUid") String turnoverUid,@RequestParam("deductionAccount") String deductionAccount,@RequestParam("rechargeAccount") String rechargeAccount, @RequestParam("amount")BigDecimal amount){
        tradeHandler.transferAccountsProcess(turnoverUid,deductionAccount,rechargeAccount,amount);
    }

}
