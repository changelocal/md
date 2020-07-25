package com.md.atom.user.service.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;


public class WxUserVO {

    @Data
    public static class Add{
        private String id;
        private String mobile;
        private String openId;
        private String appId;
        private String unionId;
        private String nickName;
        private int isEnable;
        private int followStatus;
        private Date createTime;
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
        private String mobile;
        private String openId;
        private String appId;
        private String unionId;
        private String nickName;
        private int isEnable;
        private int followStatus;
        private Date createTime;
    }
}
