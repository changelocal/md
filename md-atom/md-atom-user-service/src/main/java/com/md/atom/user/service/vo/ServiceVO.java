package com.md.atom.user.service.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class ServiceVO {

    @Data
    public static class Service {
        /**
         *
         */
        private String id;

        /**
         *
         */
        private Date createTime;

        /**
         * 服务名称
         */
        private String serviceName;

        /**
         *
         */
        private String imageUrl;

        /**
         * 商标价格
         */
        private BigDecimal price;

        /**
         * 促销折扣
         */
        private double promoteDiscount;

        /**
         * 1=不促销，2=促销
         */
        private int promoteFlag;

        /**
         * 是否上架,1=否，2=是
         */
        private int isEnable;

        /**
         * 1=待审核，2=已审核 ，3=未通过
         */
        private int isChecked;

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
         *
         */
        private String lockedOrderId;

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
         * 商标更新时间
         */
        private Date updateTime;

        /**
         *
         */
        private int totalBuyCountInc;

        /**
         *
         */
        private Date countIncUpdateTime;

        /**
         *
         */
        private long suid;

    }

    @Data
    public static class FindResp{
        private List<Service> services;
    }

}
