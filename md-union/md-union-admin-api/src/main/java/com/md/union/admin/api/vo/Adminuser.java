package com.md.union.admin.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("商标起名")
public class Adminuser {

    @Data
    @ApiModel("商标起名详情展示")
    public static class Info{
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
        private String title;

    }

    @Data
    @ApiModel("商标起名搜索")
    public static class SearchReq{

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
        private Date lastLoginTime;

        /**
         * 手机号
         */
        private String mobile;


        /**
         * 姓名
         */
        private String nickname;

        /**
         * 0=禁用，1=启用
         */
        private int isEnable;

        /**
         * QQ号
         */
        private String qqAccount;

        /**
         * 微信号
         */
        private String wxAccount;

        @ApiModelProperty("当前页")
        private int pageIndex;
        @ApiModelProperty("每页显示条数")
        private int pageSize;
    }

    @Data
    @ApiModel("商标起名查询结果")
    public static class SearchRes{
        @ApiModelProperty("分类列表")
        private List<Info> list;
        @ApiModelProperty("总条数")
        private int count;
    }


    @Data
    @ApiModel("登录返回结果")
    public static class LoginRes{
        @ApiModelProperty("token")
        private String token;
        @ApiModelProperty("name")
        private String name;
        @ApiModelProperty("user_id")
        private String user_id;
        @ApiModelProperty("access")
        private List<String> access;
        @ApiModelProperty("avatar")
        private String avatar;
    }

    @Data
    @ApiModel("登录请求参数")
    public static class LoginReq{
        private String userName;
        private String password;
    }



}
