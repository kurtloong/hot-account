package com.github.kurtloong.entrybuffer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 流水表
 * @TableName turnover_flow
 */
@Data
public class TurnoverFlow implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 流水uid
     */
    private String turnoverUid;

    /**
     * 发生交易的金额
     */
    private BigDecimal amount;

    /**
     * 账户id
     */
    private String accountId;

    /**
     * 0-入账 1-出账
     */
    private Integer turnoverType;

    /**
     * 是否入账 0-未入账 1-已入账
     */
    private Boolean turnoverFlowStatus;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public TurnoverFlow(String turnoverUid, BigDecimal amount, String accountId, Integer turnoverType) {
        this.turnoverUid = turnoverUid;
        this.amount = amount;
        this.accountId = accountId;
        this.turnoverType = turnoverType;
    }
}