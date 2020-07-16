package com.md.atom.user.service.vo;

import lombok.Data;


public class AdminUserVO {

    @Data
    public static class Add{
        private String name;
        private String mobile;
        private String qqAccount;
        private String wxAccount;
    }

    @Data
    public static class AddResp{
        private String id;

    }

}
