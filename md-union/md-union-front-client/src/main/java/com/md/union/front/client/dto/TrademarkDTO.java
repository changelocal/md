package com.md.union.front.client.dto;

import lombok.Data;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@Data
@ApiModel("购买商标相关")
public class TrademarkDTO {
    @Data
    @ApiModel("热门商标查询")
    public static class Hot{
    }
    @Data
    @ApiModel("热门商标查询结果")
    public static class HotResp{
        private List<HotTrademarkCate> hotTrademarkCates;
    }
    @Data
    public static class HotTrademarkCate{
        @ApiModelProperty("分类代号")
        private int id;
        @ApiModelProperty("分类名字")
        private String name;
        @ApiModelProperty("热门商标图标")
        private String icon;
    }
    @Data
    @ApiModel("热门商标点击查询结果")
    public static class HotClickResp{
        @ApiModelProperty("分类推荐")
        private List<HotTrademark> hotTrademarks;
    }
    @Data
    @ApiModel("热门商标点击查询结果")
    public static class HotTrademark{
        @ApiModelProperty("分类名字")
        private String name;
        @ApiModelProperty("分类推荐")
        private List<Trademark> trademarks;
    }
    @Data
    public static class Trademark {
        @ApiModelProperty("商标id")
        private String id;
        @ApiModelProperty("商标名字")
        private String name;
        @ApiModelProperty("商标图片")
        private String pic;
        @ApiModelProperty("价格区间")
        private String maxPrice;
        @ApiModelProperty("价格区间")
        private String minPrice;
        @ApiModelProperty("是否特价")
        private boolean SpecialPrice;
    }

    @Data
    @ApiModel("商标信息分页查询")
    public static class Search{
        @ApiModelProperty("商标名")
        private String name;
        @ApiModelProperty("分类")
        private String category;
        @ApiModelProperty("价格")
        private String price;
        @ApiModelProperty("组合")
        private int combination;
        @ApiModelProperty("字符")
        private int character;
        @ApiModelProperty("当前页")
        private int pageIndex;
        @ApiModelProperty("每页显示条数")
        private int pageSize;
    }
    @Data
    @ApiModel("商标信息分页查询结果")
    public static class SearchResp{
        @ApiModelProperty("总条数")
        private int total;
        @ApiModelProperty("商标列表")
        private List<Trademark> trademarklist;
    }
    @Data
    @ApiModel("发起咨询")
    public static class Consultation{
        @ApiModelProperty("商标ID")
        private String id;
    }
    @Data
    @ApiModel("咨询对象信息")
    public static class ConsultationResp{
        @ApiModelProperty("姓名")
        private String name;
        @ApiModelProperty("头衔")
        private String title;
        @ApiModelProperty("电话")
        private String tel;
        @ApiModelProperty("qq")
        private String qq;
    }
    @Data
    @ApiModel("购买")
    public static class Purchase{
        @ApiModelProperty("商标ID")
        private String id;
    }
    @Data
    @ApiModel("购买详情")
    public static class PurchaseResp{
        @ApiModelProperty("商标id")
        private String id;
        @ApiModelProperty("商标名字")
        private String name;
        @ApiModelProperty("商标图片")
        private String pic;
        @ApiModelProperty("商标分类列表")
        private List<TrademarkCate> trademarkCateList;
        @ApiModelProperty("定金")
        private String deposit;

        @ApiModelProperty("初审公告期号")
        private String firstNo;
        @ApiModelProperty("初审公告日期")
        private String firstDate;
        @ApiModelProperty("注册公告期号")
        private String signUpNo;
        @ApiModelProperty("注册公告日期")
        private String signUpDate;
        @ApiModelProperty("商标类型")
        private String trademarkType;
        @ApiModelProperty("到期时间")
        private String endDate;

        @ApiModelProperty("类似群组")
        private String likeCate;
        @ApiModelProperty("使用范围")
        private String usage;
        @ApiModelProperty("商标色系")
        private String color;
        @ApiModelProperty("商标大图")
        private String bigPic;
        @ApiModelProperty("设计理念")
        private String concept;

        @ApiModelProperty("咨询人信息")
        private ConsultationResp consultationResp;
    }
    public static class TrademarkCate {
        @ApiModelProperty("商标分类id")
        private String id;
        @ApiModelProperty("商标名字")
        private String name;
        @ApiModelProperty("价格区间")
        private String price;
    }
    @Data
    @ApiModel("商标注册详情")
    public static class SignUpDetails{
        @ApiModelProperty("商标ID")
        private String id;
    }
    @Data
    @ApiModel("购买详情")
    public static class SignUpDetailsResp{
        @ApiModelProperty("商标ID")
        private String status;
        @ApiModelProperty("商标流程")
        private List<SignUpRecord> signUpRecordList;
        @ApiModelProperty("商标公告")
        private String broadcast;
    }
    @Data
    @ApiModel("商标流程")
    public static class SignUpRecord{
        @ApiModelProperty("日期")
        private String date;
        @ApiModelProperty("内容")
        private String content;
    }
}
