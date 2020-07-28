package com.md.union.order.service;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringCloudApplication
@ComponentScan(basePackages = {"com.atom.core","com.md.union.order.service"})
public class BootStrap {

    public static void main(String... args) throws Throwable {
        SpringApplication.run(BootStrap.class, args);
    }
}
