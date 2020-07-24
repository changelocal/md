package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sage on 2019/5/8.
 */
@Data
@ApiModel("微信公众号收到消息")
public class WXMsg {

    @ApiModelProperty("ToUserName")
    private String ToUserName;
    @ApiModelProperty("FromUserName")
    private String FromUserName;
    @ApiModelProperty("MsgType")
    private String MsgType;
    @ApiModelProperty("Content")
    private String Content;
    @ApiModelProperty("event")
    private String event;
}