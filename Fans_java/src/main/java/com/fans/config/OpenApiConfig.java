package com.fans.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI marketingOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("营销转化平台 API")
                .version("1.0.0")
                .description("落地页管理、微信号管理、行为转化追踪与数据看板接口。"));
    }
}
