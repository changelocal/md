package com.md.union.front.client.dto;

import lombok.Data;

import java.util.List;


public class NameDTO {
    @Data
    public static class Info{
        private int id;
        private String brand;
        private String successRate;
        private int buyOrRegist;
    }

    @Data
    public static class SearchReq{
        private int category;
        private String input;
        private int status;
        private int wordCnt;
    }

    @Data
    public static class SearchRes{
        private List<Info> list;
        private int count;
    }
}
