package com.md.union.admin.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("商标订单相关")
public class Order {
    @ApiModel("我的订单列表查询")
    @Data
    public static class SearchReq{
        @ApiModelProperty("订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成")
        public int OrderStatus;
        @ApiModelProperty("订单类型 1商标注册订单2商标维权订单3商标信息变更订单")
        public int OrderType;
        public String OrderNo;
        @ApiModelProperty("当前页")
        public int pageIndex;
        @ApiModelProperty("每页显示条数")
        public int pageSize;
        private String[] dateRange;
    }
    @ApiModel("用户购买商标的订单列表")
    @Data
    public static class ListRes{
        @ApiModelProperty("商标订单")
        public List<OrderRes> list;
        @ApiModelProperty("总条数")
        public int total;
    }
    @ApiModel("我的订单列表")
    @Data
    public static class OrderRes{
        @ApiModelProperty("主键id")
        public int id;
        @ApiModelProperty("订单编号")
        public String orderNo;
        @ApiModelProperty("图片id")
        public String imgNo;
        @ApiModelProperty("图片url")
        public String imgUrl;
        @ApiModelProperty("商标名称")
        public String brandName;
        @ApiModelProperty("分类名称")
        public String categoryName;
        @ApiModelProperty("最高价格")
        public String maxPrice;
        @ApiModelProperty("最低价格")
        public String minPrice;
        @ApiModelProperty("定金")
        public String prePrice;
        @ApiModelProperty("订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成")
        public int status;
        public String statusName;
        @ApiModelProperty("订单类型 1商标注册订单2商标维权订单3商标信息变更订单")
        public int orderType;
        public String orderTypeName;
        /**
         * 创建时间
         */
        private Date createTime;
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
         * 产品名称
         */
        private String productName;
    }

    @ApiModel("订单提交")
    @Data
    public static class SubmitOrder{
        @ApiModelProperty("订单类型")
        public int orderType;
        @ApiModelProperty("预付款价格")
        public String prePay;
        @ApiModelProperty("尾款价格")
        public String restPay;
        @ApiModelProperty("总价")
        public String totalPay;
        @ApiModelProperty("后台运营下单人id")
        public int opUserId;
        @ApiModelProperty("产品编号")
        private String productNo;
    }

    @ApiModel("我的订单列提交资料")
    @Data
    public static class SubmitOrderFile{
        @ApiModelProperty("订单编号")
        public String orderNo;
        @ApiModelProperty("订单提交资料方式 1企业 2 个人")
        private int submitType;
        @ApiModelProperty("文件资料id")
        List<String> fileIds;
    }

    @ApiModel("订单提交")
    @Data
    public static class SubmitServiceOrder{
        @ApiModelProperty("预付款价格")
        public int prePay;
        @ApiModelProperty("总价")
        public int totalPay;
        @ApiModelProperty("后台运营下单人id")
        public long userId;
        @ApiModelProperty("产品编号")
        private String productNo;
        private int orderType;
    }
}
