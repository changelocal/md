package com.md.union.front.client.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

public class RefDTO {
    @Data
    public static class OrderRefFile {
        /**
         * 主键id
         */
        private int id;

        /**
         * 订单编号
         */
        private String orderNo;

        /**
         * 用户id
         */
        private String userNo;

        /**
         * 文件编号oss id
         */
        private String fileId;

        /**
         * 文件所属1公司2个人
         */
        private int fileSource;

        /**
         * 文件类型
         */
        private int fileType;

        /**
         * 0 有效 1 删除
         */
        private int del;

        /**
         * 创建时间
         */
        private Date createTime;

    }

    @Data
    public static class QueryResp{
        private List<OrderRefFile> orderRefFiles;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }
}