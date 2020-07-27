package com.md.atom.user.service.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;


public class WxUserVO {

    @Data
    public static class AddResp{
        private String id;
    }
    @Data
    public static class QueryResp{
        private List<WxUser> items;
        private int totalCount;
    }
    @Data
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
         * 推广快递员编号
         */
        private String shareNo;

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
}
