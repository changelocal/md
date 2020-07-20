package com.md.atom.user.service.vo;

import lombok.Data;

import java.util.List;


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
    @Data
    public static class QueryResp{
        private List<User> userList;
    }
    @Data
    public static class User{
        private String id;
        private String name;
        private String mobile;
        private String qqAccount;
        private String wxAccount;
    }
}
