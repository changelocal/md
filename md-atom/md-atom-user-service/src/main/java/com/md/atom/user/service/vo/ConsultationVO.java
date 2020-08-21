package com.md.atom.user.service.vo;

import lombok.Data;

import java.util.List;


public class ConsultationVO {

    @Data
    public static class Info{




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
        private List<Info> infos;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }
}
