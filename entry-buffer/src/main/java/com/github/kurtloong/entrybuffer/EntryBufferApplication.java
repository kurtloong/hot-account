package com.github.kurtloong.entrybuffer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author kurt.loong
 * @version 1.0
 * @date 2021/5/26 10:34
 */
@SpringBootApplication
@EnableScheduling
public class EntryBufferApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntryBufferApplication.class);
    }
}
