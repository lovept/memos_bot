package com.github.lovept;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lovept
 * @date 2024/7/23 14:30
 * @description memos bot application
 */
@SpringBootApplication
@MapperScan("com.github.lovept.mapper")
public class MemosBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemosBotApplication.class, args);
    }

}
