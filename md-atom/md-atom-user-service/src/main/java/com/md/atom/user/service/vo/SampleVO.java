package com.md.atom.user.service.vo;

import lombok.Data;


public class SampleVO {

    @Data
    public static class Test{
        private String id;
        private String name;
    }

    @Data
    public static class TestResp{
        private String id;
        private String name;
    }

}
