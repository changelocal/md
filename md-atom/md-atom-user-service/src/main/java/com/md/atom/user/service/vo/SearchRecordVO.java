package com.md.atom.user.service.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;


public class SearchRecordVO {

    @Data
    public static class Info{
        /**
         * 主键id
         */
        private int id;

        /**
         * 搜索关键字
         */
        private String searchWord;
        private String success;

        /**
         * 注册号
         */
        private String registNo;

        /**
         * 注册类别
         */
        private String registCate;

        /**
         * 未注册类别
         */
        private String unregistCate;

        /**
         * 状态 1发起搜索 2联系完成
         */
        private int status;

        /**
         * 购买用户id
         */
        private String openId;

        /**
         * 买家手机
         */
        private String buyerMobile;

        /**
         * 买家名字
         */
        private String buyerName;

        /**
         * 备注
         */
        private String note;

        /**
         * 后台运营人id被咨询者
         */
        private String opUserId;

        /**
         * 销售手机
         */
        private String opUserMobile;

        /**
         * 销售名字
         */
        private String opUserName;

        /**
         * 创建时间
         */
        private Date createTime;

        /**
         * 修改时间
         */
        private Date updateTime;

        /**
         * 当前页
         */
        private int pageIndex;
        /**
         * 每页显示条数
         */
        private int pageSize;
        private Date createTimeBegin;
        private Date createTimeEnd;
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
