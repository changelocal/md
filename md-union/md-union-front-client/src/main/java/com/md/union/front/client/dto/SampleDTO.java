package com.md.union.front.client.dto;

import lombok.Data;


public class SampleDTO {

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
