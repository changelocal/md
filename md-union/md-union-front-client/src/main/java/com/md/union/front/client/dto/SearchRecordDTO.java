package com.md.union.front.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


public class SearchRecordDTO {

    @Data
    public static class SearchRecordInfo{
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


        @ApiModelProperty("当前页")
        private int pageIndex;
        @ApiModelProperty("每页显示条数")
        private int pageSize;
        private Date createTimeBegin;
        private Date createTimeEnd;
    }

    @Data
    public static class QueryResp{
        private List<SearchRecordInfo> infos;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }
}
