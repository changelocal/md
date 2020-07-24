package com.arc.util.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

    private String wxAppid;
    private String wxSecret;
    private String minAppid;
    private String minSecret;
    private String rootHost;


}
