package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxMss {
    @Data
    @ApiModel("待支付")
    public static class ToPay {
        @ApiModelProperty("openid")
        private String openid;
        @ApiModelProperty("商品名称")
        private String name;
        @ApiModelProperty("支付")
        private String payment;
        @ApiModelProperty("订单号")
        private String orderNo;
        private String note;
    }
    @Data
    @ApiModel("生成订单")
    public static class MakeOrder {
        @ApiModelProperty("openid")
        private String openid;
        @ApiModelProperty("商品名称")
        private String name;
        @ApiModelProperty("状态")
        private String orderStatus;
        @ApiModelProperty("订单号")
        private String orderNo;
        private String orderTime;
        private String note;
    }
    @Data
    @ApiModel("物流")
    public static class Delivery {
        @ApiModelProperty("openid")
        private String openid;
        @ApiModelProperty("商品名称")
        private String name;
        @ApiModelProperty("状态")
        private String orderType;
        @ApiModelProperty("订单号")
        private String tradeType;

    }



}