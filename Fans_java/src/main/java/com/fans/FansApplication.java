package com.fans;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * fans 后端（面向广告投放的高并发营销转化平台）。
 * 默认端口 8000，与原 Vue 前端 src/utils/request.js 中的 baseURL 保持一致。
 */
@SpringBootApplication
@MapperScan("com.fans.mapper")
public class FansApplication {

    public static void main(String[] args) {
        SpringApplication.run(FansApplication.class, args);
    }
}
