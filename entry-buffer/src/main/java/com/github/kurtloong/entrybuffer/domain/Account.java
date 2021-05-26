package com.github.kurtloong.entrybuffer.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName account_1
 */
@Data
public class Account implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 账户
     */
    private String accountId;

    /**
     * 名称
     */
    private String username;

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