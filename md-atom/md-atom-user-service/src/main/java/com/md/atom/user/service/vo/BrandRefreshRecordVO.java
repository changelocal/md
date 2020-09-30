package com.md.atom.user.service.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;


public class BrandRefreshRecordVO {

    @Data
    public static class Info{
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
        private List<Info> infos;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }
}
