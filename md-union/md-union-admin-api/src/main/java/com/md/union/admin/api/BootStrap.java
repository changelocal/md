package com.md.union.admin.api;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableFeignClients({"com.md.union.front.client.feign"})
@EnableCircuitBreaker
@SpringCloudApplication
public class BootStrap {

    public static void main(String... args) throws Throwable {
        SpringApplication.run(BootStrap.class, args);
    }
}
