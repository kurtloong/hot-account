package com.github.kurtloong.entrybuffer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName balance
 */
@Data
public class Balance implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 账户
     */
    private String accountId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}