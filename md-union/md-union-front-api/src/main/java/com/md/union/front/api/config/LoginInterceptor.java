package com.md.union.front.api.config;

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


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


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
        if (request.getRequestURI().indexOf("/platform/") >= 0) {
            AppUserPrincipal principal = init(request);

            if (StrKit.isBlank(principal.getUserId()) || StrKit.isBlank(principal.getToken())) {
                throw new ServiceException("111111", "未登录");
            }

            UserPrincipal.setPrincipal(principal);
        }
        return true;
    }

    private AppUserPrincipal init(HttpServletRequest request) {
        AppUserPrincipal principal = new AppUserPrincipal();
        principal.setToken(request.getHeader("x-token"));
        //request.getSession().getAttribute("")
        log.info("初始化header成功 :{}", principal);
        return principal;
    }

    private void loadUser(){

    }


}