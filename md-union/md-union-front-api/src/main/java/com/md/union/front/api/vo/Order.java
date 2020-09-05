package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商标订单相关")
public class Order {


    @ApiModel("我的订单列表查询")
    @Data
    public static class SearchReq{
        @ApiModelProperty("当前页")
        public int pageIndex;
        @ApiModelProperty("每页显示条数")
        public int pageSize;
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
        public int OrderStatus;
        @ApiModelProperty("订单类型 1商标注册订单2商标维权订单3商标信息变更订单")
        public int OrderType;
    }

    @ApiModel("我的订单列提交资料")
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
        @ApiModelProperty("订单id")
        public String id;
        /*@ApiModelProperty("订单编号")
        public String orderNo;*/
        @ApiModelProperty("订单提交资料方式 1企业 2 个人")
        private int submitType;
        @ApiModelProperty("文件资料id")
        List<String> fileIds;
    }

    @Data
    @ApiModel("商标订单详情")
    public static class Detail{
        @ApiModelProperty("主键id")
        private int id;
        @ApiModelProperty("用户id")
        private int userId;
        @ApiModelProperty("订单编号")
        private String orderNo;
        @ApiModelProperty("图片url")
        private String imgUrl;
        @ApiModelProperty("品牌顾问信息")
        private Brand.Person person;
        @ApiModelProperty("分类名称")
        public String categoryName;
        @ApiModelProperty("支付价格")
        public String payPrice;
        @ApiModelProperty("定金")
        public String prePrice;
        @ApiModelProperty("订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成")
        public int orderStatus;
        @ApiModelProperty("下单时间")
        public String createTime;
        @ApiModelProperty("订单总价")
        public String totalPrice;
        @ApiModelProperty("商标购买的分类编号")
        private List<Category.Info> categroyList;
        @ApiModelProperty("订单完成时间")
        private String overTime;
        @ApiModelProperty("品牌名称")
        public String brandName;
        @ApiModelProperty("最低价")
        public String minPrice;
        @ApiModelProperty("最高价")
        public String maxPrice;
    }

    @Data
    @ApiModel("发起订单支付")
    public static class PayParam{
        @ApiModelProperty("订单编号")
        private String orderNo;
        @ApiModelProperty("商标编号")
        private String brandNo;
    }




}
