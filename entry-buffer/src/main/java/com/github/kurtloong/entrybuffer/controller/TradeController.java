package com.github.kurtloong.entrybuffer.controller;

import com.github.kurtloong.entrybuffer.core.EntryBufferTradeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author kurt.loong
 * @version 1.0
 * @date 2021/5/27 16:32
 */
@RestController
@RequestMapping("/trade")
public class TradeController {
    @Autowired
    EntryBufferTradeHandler tradeHandler;

    @PostMapping("/deduction")
    public void  deduction(@RequestParam("turnoverUid") String turnoverUid,@RequestParam("accountId") String accountId, @RequestParam("amount")BigDecimal amount){
        tradeHandler.deductionProcess(turnoverUid,accountId,amount);
    }

    @PostMapping("/rechargeProcess")
    public void  rechargeProcess(@RequestParam("turnoverUid") String turnoverUid,@RequestParam("accountId") String accountId, @RequestParam("amount")BigDecimal amount){
        tradeHandler.rechargeProcess(turnoverUid,accountId,amount);
    }

    @PostMapping("/transferAccountsProcess")
    public void transferAccountsProcess(@RequestParam("turnoverUid") String turnoverUid,@RequestParam("deductionAccount") String deductionAccount,@RequestParam("rechargeAccount") String rechargeAccount, @RequestParam("amount")BigDecimal amount){
        tradeHandler.transferAccountsProcess(turnoverUid,deductionAccount,rechargeAccount,amount);
    }

}
