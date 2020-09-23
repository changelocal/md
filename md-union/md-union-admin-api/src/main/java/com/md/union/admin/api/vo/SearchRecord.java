package com.md.union.admin.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("商标起名")
public class SearchRecord {

    @Data
    @ApiModel("商标起名详情展示")
    public static class Info{
        /**
         * 主键id
         */
        private int id;

        /**
         * 搜索关键字
         */
        private String searchWord;

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
        private int opUserId;

        /**
         * 销售手机
         */
        private String opUserMobile;
        private String success;

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

    }

    @Data
    @ApiModel("商标起名搜索")
    public static class SearchReq{
        /**
         * 主键id
         */
        private int id;

        /**
         * 搜索关键字
         */
        private String searchWord;

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
        private int opUserId;

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
        public int pageIndex;
        @ApiModelProperty("每页显示条数")
        public int pageSize;
        private String[] dateRange;
    }

    @Data
    @ApiModel("商标起名查询结果")
    public static class SearchRes{
        @ApiModelProperty("分类列表")
        private List<Info> list;
        @ApiModelProperty("总条数")
        private int count;
    }


}
