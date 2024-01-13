package edu.jnu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * @author Pei Qingfu
 * @version 1.0
 * @date 2022年08月12日 13:55
 */
@Configuration
@EnableOpenApi
public class Swagger3Config {

    @Value("${swagger.enable}")
    private boolean swaggerSwitch;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(swaggerSwitch)
                .groupName("郭梓繁")
                .select()
                // 过滤条件
                .apis(RequestHandlerSelectors.basePackage("edu.jnu.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "密钥中心服务器接口",
                "去重模块可信第三方密钥服务器提供的接口",
                "v1.0",
                "https://github.com/826148267",
                new Contact("郭梓繁", "https://github.com/826148267", "826148267@qq.com"),
                "Apache Licenses 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }

}
