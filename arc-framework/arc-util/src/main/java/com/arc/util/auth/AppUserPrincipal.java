package com.arc.util.auth;

/**
 * 表示站点的安全主体。
 */

public class AppUserPrincipal implements UserPrincipal {


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMinId() {
        return minId;
    }

    public void setMinId(String minId) {
        this.minId = minId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String version;
    private String appId;
    private String openId;
    private String unionId;
    private String token;
    private String minId;
    private int id;


    public static AppUserPrincipal getPrincipal() {
        UserPrincipal principal = UserPrincipal.getPrincipal();
        return (AppUserPrincipal) principal;
    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public String[] getAuthorities() {
        return new String[0];
    }


    @Override
    public String getName() {
        return null;
    }
}