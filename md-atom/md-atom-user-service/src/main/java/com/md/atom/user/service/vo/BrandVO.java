package com.md.atom.user.service.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BrandVO {
    @Data
    public static class QueryResp{
        private List<MdBrand> mdBrands;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }
    @Data
    public static class MdBrand {
        /**
         * 商标价格区间下限
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
    }
}