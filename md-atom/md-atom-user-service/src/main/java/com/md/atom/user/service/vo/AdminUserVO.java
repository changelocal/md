package com.md.atom.user.service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


public class AdminUserVO {

    @Data
    public static class AdminUser{
        private int id;

        /**
         *
         */
        private Date createTime;

        /**
         *
         */
        private String account;

        /**
         *
         */
        private String password;

        /**
         * 0=god,
         */
        private int type;



        /**
         * 角色
         */
        private String role;

        /**
         *
         */
        private String email;

        /**
         *
         */
        private Date lastLoginTime;

        /**
         * 手机号
         */
        private String mobile;

        /**
         * 微信-openid 用于微信二维码登录
         */
        private String wxId;

        /**
         *
         */
        private String avatar;

        /**
         * 姓名
         */
        private String nickname;

        /**
         * 0=禁用，1=启用
         */
        private int isEnable;

        /**
         * 备注
         */
        private String remark;

        /**
         * QQ号
         */
        private String qqAccount;

        /**
         * 微信号
         */
        private String wxAccount;

        /**
         * 微信二维码名片
         */
        private String wxQrcode;
        private String title;
        private String salt;

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
        private List<AdminUser> adminUsers;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }

    @Data
    @ApiModel("登录返回结果")
    public static class HomeRes{
        @ApiModelProperty("title")
        private String title;
        @ApiModelProperty("icon")
        private String icon;
        @ApiModelProperty("count")
        private long count;
        @ApiModelProperty("color")
        private String color;
    }
}
