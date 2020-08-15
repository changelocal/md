package com.arc.util.auth;

import com.arc.util.context.InvokeContextHolder;
import com.arc.util.lang.StrKit;

import java.security.Principal;
import java.util.Objects;

/**
 * 用户安全主体。
 */
public interface UserPrincipal extends Principal {

    class InnerHelper {
        //
        public static final String IC_KEY_CURRENT_PRINCIPAL = "CurrentPrincipal";
    }

    public static void setPrincipal(UserPrincipal principal) {
        InvokeContextHolder.current().set(InnerHelper.IC_KEY_CURRENT_PRINCIPAL, principal);
    }

    /**
     * 获取当前安全主体。
     *
     * @param useAnonymous 如果没有当前上下文没有关联的安全主体，是否返回匿名用户主体。
     * @return
     */
    public static UserPrincipal getPrincipal(boolean useAnonymous) {
        UserPrincipal p = getPrincipal();
        if (p == null && useAnonymous) {
            p = AnonymousUserPrincipal.getInstance();
        }
        return p;
    }

    /**
     * 获取当前安全主体。
     * 如果没有当前上下文没有关联的安全主体，则返回 null。
     *
     * @return
     */
    public static UserPrincipal getPrincipal() {
        return InvokeContextHolder.current().get(InnerHelper.IC_KEY_CURRENT_PRINCIPAL);
    }

    /**
     * 获取当前安全主体。
     * 如果没有当前上下文没有关联的安全主体，则返回抛出 IllegalStateException 。
     *
     * @return
     */
    public static UserPrincipal currentPrincipal() {
        UserPrincipal p = getPrincipal();
        if (p == null) {
            throw new IllegalStateException("not bound-UserPrincipal found");
        }
        return p;
    }

    /**
     * 获取关联的用户ID
     *
     * @return
     */
    String getUserId();

    /**
     * 获取关联的授权列表
     *
     * @return
     */
    String[] getAuthorities();

    /**
     * 身份是否已验证过。
     * 缺省实现：如果 loginUserId 不为零则认为已验证。
     *
     * @return
     */
    public default boolean isAuthenticated() {
        return StrKit.notBlank(getUserId());
    }

    /**
     * 是否具有指定功能访问权限
     *
     * @param authority
     * @return
     */
    public default boolean isAuthorized(String authority) {
        Objects.requireNonNull(authority, "arg authority");
        String[] authorities = getAuthorities();
        if (authorities != null && authorities.length > 0) {
            for (String r : authorities) {
                if (r.equalsIgnoreCase(authority)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否具有指定所有功能的访问权限，即 AND 关系。
     *
     * @param authorities
     * @return
     */
    public default boolean isAllAuthorized(String... authorities) {
        Objects.requireNonNull(authorities, "arg authorities");
        for (String f : authorities) {
            if (!isAuthorized(f)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否具有指定功能列表任意访问权限，即 OR 关系。
     *
     * @param authorities
     * @return
     */
    public default boolean isAnyAuthorized(String... authorities) {
        Objects.requireNonNull(authorities, "arg authorities");
        for (String f : authorities) {
            if (isAuthorized(f)) {
                return true;
            }
        }
        return false;
    }

}
