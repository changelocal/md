package com.md.union.front.api;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableFeignClients({"com.md.union.front.client.feign"})
@SpringCloudApplication
public class BootStrap {

    public static void main(String... args) throws Throwable {
        SpringApplication.run(BootStrap.class, args);
    }
}
