package com.md.union.front.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


public class WxUserDTO {

    @Data
    public static class AddResp{
        private String id;
    }
    @Data
    public static class Resp{
        private String id;
    }
    @Data
    public static class QueryResp{
        private List<WxUser> items;
        private int totalCount;
    }

    @Data
    @ApiModel("微信用户实体")
    public static class WxUser{
        /**
         * 主键
         */
        private long id;

        /**
         * 用户唯一标识id
         */
        private String appId;

        /**
         * 联合id
         */
        private String unionId;

        /**
         * 微信公众号openId
         */
        private String openId;

        /**
         * 微信小程序openId
         */
        private String minId;

        /**
         * 是否关注(1关注 2取消关注)
         */
        private int followStatus;

        /**
         * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
         */
        private int sex;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 真实姓名
         */
        private String realName;

        /**
         * 身份证号
         */
        private String idCard;

        /**
         * 手机
         */
        private String mobile;

        /**
         * 联系地址
         */
        private String address;

        /**
         * 关注时间
         */
        private Date createTime;

        /**
         * 商户编号
         */
        private String businessNo;
        @ApiModelProperty("当前页")
        private int pageIndex;
        @ApiModelProperty("每页显示条数")
        private int pageSize;
    }

    @Data
    @ApiModel("查询微信用户实体")
    public static class QueryWxUser{
        /**
         * 主键
         */
        private long id;

        /**
         * 用户唯一标识id
         */
        private String appId;

        /**
         * 联合id
         */
        private String unionId;

        /**
         * 微信公众号openId
         */
        private String openId;

        /**
         * 微信小程序openId
         */
        private String minId;

        /**
         * 是否关注(1关注 2取消关注)
         */
        private int followStatus;

        /**
         * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
         */
        private int sex;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 真实姓名
         */
        private String realName;

        /**
         * 身份证号
         */
        private String idCard;

        /**
         * 手机
         */
        private String mobile;

        /**
         * 联系地址
         */
        private String address;

        /**
         * 关注时间
         */
        private Date createTime;

        /**
         * 商户编号
         */
        private String businessNo;

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
    @ApiModel("修改微信用户实体")
    public static class UpdateWxUser{
        /**
         * 主键
         */
        private long id;

        /**
         * 用户唯一标识id
         */
        private String appId;

        /**
         * 联合id
         */
        private String unionId;

        /**
         * 微信公众号openId
         */
        private String openId;

        /**
         * 微信小程序openId
         */
        private String minId;

        /**
         * 是否关注(1关注 2取消关注)
         */
        private int followStatus;

        /**
         * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
         */
        private int sex;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 真实姓名
         */
        private String realName;

        /**
         * 身份证号
         */
        private String idCard;

        /**
         * 手机
         */
        private String mobile;

        /**
         * 联系地址
         */
        private String address;

        /**
         * 关注时间
         */
        private Date createTime;

        /**
         * 修改时间
         */
        private Date updateTime;

        /**
         * 商户编号
         */
        private String businessNo;
    }

}
