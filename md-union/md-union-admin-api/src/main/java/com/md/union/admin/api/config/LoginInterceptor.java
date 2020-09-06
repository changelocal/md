package com.md.union.admin.api.config;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.auth.Anonymous;
import com.arc.util.auth.AppUserPrincipal;
import com.arc.util.auth.UserPrincipal;
import com.arc.util.lang.StrKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static Map<String, String> loginStatus = new HashMap<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 优先方法上的注解 @Authorize
        Anonymous authorizeOnMethod = handlerMethod.getMethodAnnotation(Anonymous.class);
        if (authorizeOnMethod != null) {
            return true;
        }
        // 类上的注解 @Authorize
        Anonymous authorizeOnType = handlerMethod.getBeanType().getAnnotation(Anonymous.class);
        if (authorizeOnType != null) {
            return true;
        }
        response.setContentType("application/json");
        AppUserPrincipal principal = init(request);
        UserPrincipal.setPrincipal(principal);
        if (request.getRequestURI().indexOf("/front/pay/notifyUrl") >= 0 ||
                request.getRequestURI().indexOf("/front/login/min") >= 0) {

        } else {
            if (StrKit.isBlank(principal.getToken()) || principal.getId() == 0
                    || StrKit.isBlank(principal.getMinId()) || "111".equals(principal.getMinId())) {
                throw new ServiceException("111111", "未登录");
            }
        }
        return true;
    }

    private AppUserPrincipal init(HttpServletRequest request) {
        AppUserPrincipal principal = new AppUserPrincipal();
        principal.setToken(request.getHeader("x-token"));
        //request.getSession().getAttribute("")
        /*if (loginStatus.containsKey(principal.getToken())) {
            MinUser minUser = JSON.parseObject(loginStatus.get(principal.getToken()), MinUser.class);
            principal.setOpenId(minUser.getOpenId());
            principal.setAppId(minUser.getAppId());
            principal.setId(minUser.getId());
            principal.setMinId(minUser.getMinId());
        } else {
            *//*principal.setToken("1111");
            principal.setId(158);
            principal.setMinId("o2zlA5aTsMy2Bkv63V5C4zIidHPE");*//*
        }*/
        log.info("登录用户上下文对象信息 :{}", JSON.toJSONString(principal));

        return principal;
    }


}