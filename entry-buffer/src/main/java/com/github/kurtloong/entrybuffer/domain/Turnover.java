package com.github.kurtloong.entrybuffer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 流水表
 * @TableName turnover
 */
@Data
public class Turnover implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 发生交易的账户
     */
    private String accountId;

    /**
     * 上游产生的流水号
     */
    private String turnoverUid;

    /**
     * 0入账 1出账
     */
    private Integer turnoverType;

    /**
     * 版本号 -链式设计
     */
    private Integer turnoverVersion;

    /**
     * 流水发生之前的余额
     */
    private BigDecimal beforeBalance;

    /**
     * 发生交易的金额
     */
    private BigDecimal amount;

    /**
     * 发生流水之后的金额
     */
    private BigDecimal afterBalance;

    /**
     * 0-未入账 1-已入账
     */
    private Boolean turnoverStatus;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;


}