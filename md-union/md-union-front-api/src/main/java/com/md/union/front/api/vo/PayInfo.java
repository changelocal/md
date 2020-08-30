package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("支付")
public class PayInfo {

    @Data
    @ApiModel("发起支付结果")
    public static class Order{
        @ApiModelProperty("appId")
        private String appId;
        @ApiModelProperty("nonceStr")
        private String nonceStr;
        @ApiModelProperty("prePayId")
        private String prePayId;
        @ApiModelProperty("signType")
        private String signType;
        @ApiModelProperty("timeStamp")
        private String timeStamp;
        @ApiModelProperty("paySign")
        private String paySign;
    }




}
