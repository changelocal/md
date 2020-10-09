package com.md.union.admin.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("微信小程序登录用户")
public class MinUser {

    @ApiModelProperty("appId 用户唯一标识")
    private String appId;
    @ApiModelProperty("openId")
    private String openId;
    @ApiModelProperty("微信小程序openId")
    private String minId;
    @ApiModelProperty("sessionId")
    private String sessionId;
    @ApiModelProperty("unionId")
    private String unionId;
    @ApiModelProperty("sessionKey")
    private String sessionKey;
    @ApiModelProperty("授权类型 1微信公众号  2微信小程序 3手机号登录")
    private int type;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("主键id")
    private long id;


}