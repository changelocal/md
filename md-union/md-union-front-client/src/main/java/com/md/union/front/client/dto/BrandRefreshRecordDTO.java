package com.md.union.front.client.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;


public class BrandRefreshRecordDTO {

    @Data
    public static class BrandRefreshRecordInfo{
        /**
         * 主键id
         */
        private int id;

        /**
         * 备注
         */
        private String note;

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
    public static class QueryResp{
        private List<BrandRefreshRecordInfo> infos;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }
}
