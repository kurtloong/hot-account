package com.github.kurtloong.entrybuffer.util;

/**
 * @author kurt.loong
 * @version 1.0
 * @date 2021/5/27 15:51
 */
public class TableUtil {

    /**
     * 暂时写死代替
     *
     * @param accountId the account id
     * @return the string
     */
    public static String getTableName(String accountId){
        switch (accountId){
            case "ACCOUNT2":
                return "balance2";
            default:
                return "balance1";
        }
    }
}
