package com.arc.common.aspect;

import com.arc.common.annotation.ReceiveRequestOnly;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReceiveRequestOnlyInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        if (HandlerMethod.class.isInstance(handler)
                && ((HandlerMethod) handler).hasMethodAnnotation(ReceiveRequestOnly.class)) {
            response.setHeader("X-Recv-Request-Only", "true");
        }
    }
}
