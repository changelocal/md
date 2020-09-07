package com.md.atom.user.service.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;


public class ConsultationVO {

    @Data
    public static class Info{
        /**
         * 主键id
         */
        private int id;

        /**
         * 咨询订单编号
         */
        private String orderNo;

        /**
         * 订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成
         */
        private int status;

        /**
         * 预付款价格
         */
        private int prePay;

        /**
         * 购买用户id
         */
        private String openId;

        /**
         * 后台运营人id被咨询者
         */
        private String opUserId;

        /**
         * 创建时间
         */
        private Date createTime;

        /**
         * 修改时间
         */
        private Date updateTime;

        private String note;
        /**
         * 当前页
         */
        private int pageIndex;
        /**
         * 每页显示条数
         */
        private int pageSize;
    }

    @Data
    public static class QueryResp{
        private List<Info> infos;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }
}
