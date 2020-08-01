package com.md.atom.user.service.vo;

import lombok.Data;

import java.util.List;


public class BrandClassVO {

    @Data
    public static class Cate{
        private int code;
        private String categoryName;
        private String des;

    }

    @Data
    public static class HotResp{
        private List<Cate> cates;

    }

}
