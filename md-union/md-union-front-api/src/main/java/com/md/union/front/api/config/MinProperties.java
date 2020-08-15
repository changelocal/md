package com.md.union.front.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "weixin.min")
public class MinProperties {

    private String routeHost;
    private String minAppId ;
    private String minSecret ;
    private String mchId;
}
