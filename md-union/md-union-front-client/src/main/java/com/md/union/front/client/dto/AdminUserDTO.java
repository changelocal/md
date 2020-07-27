package com.md.union.front.client.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;


public class AdminUserDTO {

    @Data
    public static class AdminUser{
        private String id;

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
}
