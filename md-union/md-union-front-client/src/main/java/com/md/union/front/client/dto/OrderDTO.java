package com.md.union.front.client.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("订单相关")
public class OrderDTO {

    @Data
    @ApiModel("订单列表返回结果")
    public static class QueryResp{
        private List<BrandOrderVO> items;
        private int totalCount;
    }
    @Data
    @ApiModel("返回结果")
    public static class Resp{
        private int code;
    }
    @Data
    @ApiModel("用户支付订单")
    public static class BrandOrderVO {
        /**
         * 主键id
         */
        private int id;

        /**
         * 订单编号
         */
        private String orderNo;

        /**
         * 订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成
         */
        private int status;

        /**
         * 1商标注册订单2商标维权订单3商标信息变更订单
         */
        private int orderType;

        /**
         * 预付款价格
         */
        private int prePay;

        /**
         * 尾款价格
         */
        private int restPay;

        /**
         * 总价
         */
        private int totalPay;

        /**
         * 购买用户id
         */
        private long userId;

        /**
         * 后台运营下单人id
         */
        private int opUserId;

        /**
         * 创建时间
         */
        private Date createTime;

        /**
         * 修改时间
         */
        private Date updateTime;

        /**
         * 预支付时间
         */
        private Date preTime;

        /**
         * 尾款支付时间
         */
        private Date overTime;

        /**
         * 产品编号
         */
        private String productNo;
        /**
         * 当前页
         */
        private int pageIndex;
        /**
         * 每页显示条数
         */
        private int pageSize;
        /**
         * 最低价
         */
        private int minPrice;
        /**
         * 最高价
         */
        private int maxPrice;
        /**
         * 分类id
         */
        private int category;
        /**
         * 分类名称
         */
        private String categoryName;

        /**
         * 产品名称
         */
        private String productName;
        /**
         * 品牌图片
         */
        private String img;
    }

    @Data
    public static class SubOrderReq{
        private int orderType;
        private String code;
    }
}
