package com.arc.util.auth;

/**
 * 表示站点的安全主体。
 */

public class AppUserPrincipal implements UserPrincipal {

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    private String sign;
    private String version;
    private String userId;
    private String openId;
    private String unionId;
    private String token;


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