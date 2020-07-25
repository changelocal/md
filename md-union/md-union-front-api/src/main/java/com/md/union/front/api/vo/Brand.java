package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("商标")
@Data
public class Brand {


    @Data
    @ApiModel("热门商标分类")
    public static class HotRes {
        @ApiModelProperty("主键id")
        private String id;
        @ApiModelProperty("商标分类icon")
        private String icon;
        @ApiModelProperty("商标分类名称")
        private String categoryName;
    }

    @Data
    @ApiModel("商标严选")
    public static class GroupRes {
        @ApiModelProperty("分组名称")
        private String name;
        @ApiModelProperty("分组商标列表")
        private List<SpecialRes> list;
    }

    @Data
    @ApiModel("列表商标信息")
    public static class SpecialRes {
        @ApiModelProperty("主键id")
        private String id;
        @ApiModelProperty("分类名称")
        private String categoryName;
        @ApiModelProperty("图片url")
        private String imgUrl;
        @ApiModelProperty("商标名称")
        public String brandName;
        @ApiModelProperty("最高价格")
        public String maxPrice;
        @ApiModelProperty("最低价格")
        public String minPrice;
        @ApiModelProperty("是否是特价")
        public boolean special;
    }

    @Data
    @ApiModel("购买商标查询")
    public static class SearchReq {
        @ApiModelProperty("商标名称")
        private String brandName;
        @ApiModelProperty("商标分类编号")
        private String categoryNo;
        @ApiModelProperty("价格类型")
        private String priceType;
        @ApiModelProperty("商标组合分类")
        private int unionType;
        @ApiModelProperty("商标字符类型 0不限 1(1-2个字) 2(3个字) 3(4个字) 5(5个字以上) ")
        private int brandSize;
        @ApiModelProperty("当前页")
        public int pageIndex;
        @ApiModelProperty("每页显示条数")
        public int pageSize;
    }

    @Data
    @ApiModel("购买商标查询结果")
    public static class SearchRes {
        @ApiModelProperty("查询结果list")
        private List<SpecialRes> list;
        @ApiModelProperty("总条数")
        private int total;
    }

    @Data
    @ApiModel("商标详情购买展示")
    public static class DetailBuy {
        @ApiModelProperty("商标可以购买的分类编号")
        private List<Category> categroyList;
        @ApiModelProperty("商标名称")
        private String brandName;
        @ApiModelProperty("商标主键id")
        private int id;
        @ApiModelProperty("图片url")
        private String imgUrl;
        @ApiModelProperty("品牌顾问信息")
        private Person person;
        @ApiModelProperty("品牌介绍待定")
        private String BrandRecommendText;
        @ApiModelProperty("购买流程介绍待定")
        private String BuyRecommendText;
    }

    @Data
    @ApiModel("品牌顾问")
    public static class Person {
        @ApiModelProperty("头像")
        private String headImg;
        @ApiModelProperty("姓名")
        private String name;
        @ApiModelProperty("电话")
        private String phone;
        @ApiModelProperty("QQ号码")
        private String qq;
    }

    @Data
    @ApiModel("商标购买详情")
    public static class DealDetail {
        @ApiModelProperty("图片url")
        private String imgUrl;
        @ApiModelProperty("品牌顾问信息")
        private Person person;
        @ApiModelProperty("购买价格")
        public String buyPrice;
        @ApiModelProperty("市场价")
        public String marketPrice;
        @ApiModelProperty("累计成交")
        public int total;
    }

    @Data
    @ApiModel("商标注册报价介绍")
    public static class BrandRegister {
        @ApiModelProperty("主题")
        private String title;
        @ApiModelProperty("简介")
        private String brief;
        @ApiModelProperty("报价")
        private String priceDesc;
        @ApiModelProperty("商标分类")
        private int brandType;
        @ApiModelProperty("主键id")
        private int id;
    }

    @Data
    @ApiModel("商标维权报价介绍信息详情")
    public static class BrandRight {
        @ApiModelProperty("主键id")
        private int id;
        @ApiModelProperty("介绍图片")
        private String img;
        @ApiModelProperty("主题")
        private String title;
        @ApiModelProperty("简介")
        private String brief;
        @ApiModelProperty("报价")
        private String priceDesc;
        @ApiModelProperty("商标分类")
        private int brandType;
    }

    @Data
    @ApiModel("商标维权报价介绍")
    public static class DealRight {
        @ApiModelProperty("品牌顾问")
        private Person person;
        @ApiModelProperty("品牌权益")
        private List<BrandRight> rights;
        @ApiModelProperty("品牌信息变更")
        private List<BrandRight> changes;

    }

    @Data
    @ApiModel("展示商标购买详情")
    public static class Detail{
        @ApiModelProperty("图片url")
        private String imgUrl;
        @ApiModelProperty("品牌顾问信息")
        private Brand.Person person;
        @ApiModelProperty("分类名称")
        public String categoryName;
        @ApiModelProperty("最高价格")
        public String maxPrice;
        @ApiModelProperty("最低价格")
        public String minPrice;
        @ApiModelProperty("定金")
        public String prePrice;
        @ApiModelProperty("商标购买的分类编号")
        private List<Category.Info> categroyList;
        @ApiModelProperty("商标状态")
        private int Brandstatus;
        @ApiModelProperty("商标状态描述 1提交申请，2审核中，3初审公告，4注册成功，5商标无效")
        private int BrandstatusDesc;
    }

}
