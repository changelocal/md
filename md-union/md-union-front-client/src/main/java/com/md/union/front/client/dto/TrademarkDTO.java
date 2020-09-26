package com.md.union.front.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
        private List<HotTrademarkCate> cates;
    }
    @Data
    public static class HotTrademarkCate{
        @ApiModelProperty("分类代号")
        private int code;
        @ApiModelProperty("分类名字")
        private String categoryName;
        @ApiModelProperty("热门商标图标")
        private String icon;
        @ApiModelProperty("是否有同名商标1：不显示多  2：显示多")
        private int multiName;
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
        private int id;
    }
    @Data
    @ApiModel("咨询对象信息")
    public static class ConsultationResp{
        @ApiModelProperty("主键id")
        private int id;
        @ApiModelProperty("姓名")
        private String nickname;
        @ApiModelProperty("角色")
        private String role;
        @ApiModelProperty("电话")
        private String mobile;
        @ApiModelProperty("qq")
        private String qqAccount;
        @ApiModelProperty("头衔")
        private String title;
        @ApiModelProperty("头像")
        private String avatar;
    }
    @Data
    @ApiModel("购买")
    public static class Purchase{
        @ApiModelProperty("商标名称")
        private String name;
    }
    @Data
    @ApiModel("购买详情")
    public static class PurchaseResp{
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

        @ApiModelProperty("咨询人信息")
        private ConsultationResp consultationResp;
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
        @ApiModelProperty("商标分类id")
        private String cateCode;
        @ApiModelProperty("商标分类名字")
        private String cateName;
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

    @Data
    public static class Cate{
        private int code;
        private String categoryName;
        private String des;
    }

    @Data
    public static class RootBrandResp{
        private List<Cate> cates;
    }
    @Data
    public static class RootBrandReq{
        private int code;
    }
    @Data
    public static class Resp{

        private int code;
    }
    @Data
    public static class QueryResp{
        private List<MdBrand> mdBrands;
        private int total;
    }
    @Data
    public static class MdBrand {
        /**
         * 商标价格区间上限
         */
        private BigDecimal priceLow;
        /**
         * 商标价格区间上限
         */
        private BigDecimal priceHigh;
        /**
         *
         */
        private String id;

        /**
         *
         */
        private Date createTime;
        private Date createTimeBegin;
        private Date createTimeEnd;

        /**
         * 商标名称
         */
        private String brandName;

        /**
         * 商标ID
         */
        private String brandId;

        /**
         * 商标组合类型
         */
        private int comType;

        /**
         * 有效期限-开始时间
         */
        private Date timeLimitStart;

        /**
         * 有效期限-结束时间
         */
        private Date timeLimitEnd;

        /**
         * 商标所属类别
         */
        private int category;

        /**
         * 商标所属类别名称
         */
        private String categoryName;

        /**
         * 商标类似群组
         */
        private String group;

        /**
         * 适用项目
         */
        private String fitProjects;

        /**
         *
         */
        private String fitCategory;

        /**
         *
         */
        private String fitEshop;

        /**
         * 初审公告期号
         */
        private String firstCheckId;

        /**
         * 初审公告日期
         */
        private Date firstCheckTime;

        /**
         * 注册公告期号
         */
        private String registerCheckId;

        /**
         * 注册公告日期
         */
        private Date registerCheckTime;

        /**
         *
         */
        private String brandType;

        /**
         * 优先权日期
         */
        private Date priorityTime;

        /**
         * 后期指定日期
         */
        private Date nextPointTime;

        /**
         * 国际注册日期
         */
        private Date internationalRegisterTime;

        /**
         * 专用权开始时间
         */
        private Date ownTimeStart;

        /**
         * 专用权结束时间
         */
        private Date ownTimeEnd;

        /**
         *
         */
        private String imageUrl;

        /**
         * 商标价格
         */
        private BigDecimal price;

        /**
         *
         */
        private BigDecimal promotePrice;

        /**
         *
         */
        private Date promoteEndTime;

        /**
         *
         */
        private Date promoteStartTime;

        /**
         * 促销折扣
         */
        private Double promoteDiscount;

        /**
         * 1=不促销，2=促销
         */
        private int promoteFlag;

        /**
         *
         */
        private String detailImages;

        /**
         * 交易类型1=转让，2=授权
         */
        private int tradeType;

        /**
         * 提取主题色
         */
        private String themeColor;

        /**
         * 是否公用商标 1 = 否， 2=是
         */
        private int isShareBrand;

        /**
         * 1=不推荐，2=推荐
         */
        private int isRecommend;

        /**
         * 是否上架,1=否，2=是
         */
        private int isEnable;

        /**
         * 是否精品，1=否，2=是
         */
        private int isQuality;

        /**
         * 1=待审核，2=已审核 ，3=未通过
         */
        private int isChecked;

        /**
         * 1=可用，2=已删除，2=彻底删除
         */
        private int isDelete;

        /**
         *
         */
        private String videoUrl;

        /**
         * 视频是否是默认图 1 = 否，2 = 是
         */
        private int isVideoDefault;

        /**
         * 设计理念
         */
        private String des;

        /**
         * 1=未售，2=已售，3=被锁定
         */
        private int isSale;

        /**
         *
         */
        private String lockedOrderId;

        /**
         * 1=商标，2=服务
         */
        private int type;

        /**
         * 商标名称长度
         */
        private int brandNameLength;
        /**
         * 商标名称长度
         */
        private int brandNameLengthLow;
        /**
         * 商标名称长度
         */
        private int brandNameLengthHigh;
        /**
         * 副标题
         */
        private String subTitle;

        /**
         *
         */
        private int totalBuyCount;

        /**
         * 市场价
         */
        private BigDecimal marketPrice;

        /**
         *
         */
        private String serviceTypeId;

        /**
         *
         */
        private String serviceTypeName;

        /**
         *
         */
        private String key;

        /**
         *
         */
        private String regNo;

        /**
         * 同行底价
         */
        private BigDecimal peersPrice;

        /**
         * 商标性质
         */
        private String brandNatrue;

        /**
         * 商标持有人
         */
        private String regMan;

        /**
         * 商标持有人
         */
        private String holderMan;

        /**
         *
         */
        private String contact;

        /**
         * 1=爬虫爬取-中华商标超市，2=标库网入库，3=excel导入，4=爬虫爬取-金典商标，5=爬虫爬取-标博士，6=爬虫爬取-金牌商标
         */
        private int importType;

        /**
         *
         */
        private String exportUserId;

        /**
         * 1=不置顶，2=置顶
         */
        private int isTop;

        /**
         * 商标更新时间
         */
        private Date updateTime;

        /**
         * 1=需要咨询，2=不需要咨询
         */
        private int needAsk;

        /**
         * 代理人/代理机构
         */
        private String agent;

        /**
         *
         */
        private int totalBuyCountInc;

        /**
         *
         */
        private Date countIncUpdateTime;

        /**
         * 1=不是同名，2=是同名
         */
        private int multiName;

        /**
         *
         */
        private int suid;
        /**
         * 当前页
         */
        private int pageIndex;
        /**
         * 每页显示条数
         */
        private int pageSize;

        /**
         * 商标编号
         */
        private List<String> brandIds;
        private List<String> groups;
    }
}
