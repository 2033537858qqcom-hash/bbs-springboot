package com.liang.bbs.rest.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
public class SwaggerConfig {

    /**
     * 鏂囨。棣栭〉姒傝堪
     *
     * @return
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("南生论坛（开源版/个人版）Restful API 文档")
                        .version(ApiVersionConstant.V_LATEST)
                        .description("南生论坛是南生系列开源系统的一部分，用于提供论坛相关接口文档。")
                        .contact(new Contact().name("马亮南生")
                                .url("https://github.com/maliangnansheng")
                                .email("924818949@qq.com")));
    }

//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("default")
//                .pathsToMatch("/api/**")
//                .build();
//    }


}
