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
        private int code;
        @ApiModelProperty("商标分类icon")
        private String icon;
        @ApiModelProperty("商标分类名称")
        private String categoryName;
        @ApiModelProperty("分类")
        private String typeName;
        @ApiModelProperty("是否有同名商标1：不显示多  2：显示多")
        private int multiName;
    }

    @Data
    @ApiModel("商标严选")
    public static class GroupRes {
        @ApiModelProperty("分组id")
        private int code;
        @ApiModelProperty("分组名字")
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
        @ApiModelProperty("是否有同名商标1：不显示多  2：显示多")
        private int multiName;
    }

    @Data
    @ApiModel("购买商标查询")
    public static class SearchReq {
        @ApiModelProperty("商标名称")
        private String brandName;
        @ApiModelProperty("商标分类编号")
        private int categoryNo;
        @ApiModelProperty("价格类型")
        private int priceType;
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
    @ApiModel("购买商标查询结果")
    public static class HotList {
        @ApiModelProperty("八大分类")
        private List<Brand.HotRes> cate;
        @ApiModelProperty("各分类下的商标")
        private List<Brand.GroupRes> group;
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
        @ApiModelProperty("头衔")
        private String title;
        @ApiModelProperty("主键id")
        private int id;
    }

    @Data
    @ApiModel("商标购买详情")
    public static class DealDetail {
        private String id;
        private String orderType;
        @ApiModelProperty("title")
        private String title;
        @ApiModelProperty("subTitle")
        private String subTitle;
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
        @ApiModelProperty("详细")
        public String des;
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
        private String id;
        @ApiModelProperty("icon")
        private String icon;
    }

    @Data
    @ApiModel("商标维权报价介绍信息详情")
    public static class BrandRight {
        @ApiModelProperty("主键id")
        private String id;
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
    @ApiModel("商标维权报价介绍信息详情")
    public static class BrandDesign {
        @ApiModelProperty("主键id")
        private String id;
        @ApiModelProperty("icon")
        private String icon;
        @ApiModelProperty("主题")
        private String title;
        @ApiModelProperty("简介")
        private String brief;
    }

    @Data
    @ApiModel("商标维权-品牌权益")
    public static class BrandIcon{
        @ApiModelProperty("主键id")
        private String id;
        @ApiModelProperty("图标")
        private String icon;
        @ApiModelProperty("标题")
        private String title;
        @ApiModelProperty("商标分类")
        private int brandType;
    }

    @Data
    @ApiModel("设计介绍")
    public static class Design {
        @ApiModelProperty("品牌权益")
        private List<BrandDesign> designs;
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
        @ApiModelProperty("商标分类列表")
        private List<TrademarkCate> trademarkCateList;
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
        @ApiModelProperty("品牌顾问信息")
        private Brand.Person person;
        private String brandNo;
        private int OrderType;
        private List<FlowInfo> flowInfos;
    }
    @Data
    public static class FlowInfo{
        private String date;
        private String name;
    }
    @Data
    @ApiModel("展示商标详情")
    public static class RegistDetail{
        @ApiModelProperty("商标分类列表")
        private List<TrademarkCate> trademarkCateList;
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
        @ApiModelProperty("品牌顾问信息")
        private Brand.Person person;

    }
    @Data
    @ApiModel("展示查商标的详情")
    public static class SearchDetail{
        @ApiModelProperty("成功率")
        private String success;
        @ApiModelProperty("总数")
        private int total;

        @ApiModelProperty("商标分类列表")
        private List<TrademarkCateSearch> trademarkCateListRegist;
        @ApiModelProperty("注册了的分类")
        private int registCount;
        @ApiModelProperty("商标分类列表")
        private List<TrademarkCateSearch> trademarkCateListUnRegist;
        @ApiModelProperty("未注册了的分类")
        private int unRegistCount;

        @ApiModelProperty("相似商标")
        private List<SpecialRes> familiar;
    }
    @Data
    @ApiModel("商标注册详情")
    public static class TrademarkCateSearch {
        @ApiModelProperty("商标id")
        private String id;
        @ApiModelProperty("商标名字")
        private String name;
        @ApiModelProperty("商标分类id")
        private int cateCode;
        @ApiModelProperty("商标分类名字")
        private String cateName;
    }

    @Data
    @ApiModel("商标注册详情")
    public static class TrademarkCate {
        @ApiModelProperty("商标id")
        private String id;
        @ApiModelProperty("商标名字")
        private String name;
        @ApiModelProperty("商标图片")
        private String pic;
        @ApiModelProperty("定金")
        private String deposit;
        @ApiModelProperty("价格")
        private String priceLow;
        @ApiModelProperty("价格")
        private String priceHigh;
        @ApiModelProperty("商标分类id")
        private int cateCode;
        @ApiModelProperty("商标分类名字")
        private String cateName;
        @ApiModelProperty("是否有同名商标1：不显示多  2：显示多")
        private int multiName;
    }
    @Data
    @ApiModel("权威认证")
    public static class PowerAuth{
        @ApiModelProperty("主键id")
        private int id;
        @ApiModelProperty("图标")
        private String img;
        @ApiModelProperty("标题")
        private String title;
    }

}
