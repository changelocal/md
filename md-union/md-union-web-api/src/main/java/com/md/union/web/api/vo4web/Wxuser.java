package com.md.union.web.api.vo4web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("商标起名")
public class Wxuser {

    @Data
    @ApiModel("商标起名详情展示")
    public static class Info{
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

    }

    @Data
    @ApiModel("商标起名搜索")
    public static class SearchReq{

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


}
