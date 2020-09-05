package com.md.union.web.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("商标起名")
public class Consultation {

    @Data
    @ApiModel("商标起名详情展示")
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

    }

    @Data
    @ApiModel("商标起名搜索")
    public static class SearchReq{

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

        @ApiModelProperty("当前页")
        public int pageIndex;
        @ApiModelProperty("每页显示条数")
        public int pageSize;
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
