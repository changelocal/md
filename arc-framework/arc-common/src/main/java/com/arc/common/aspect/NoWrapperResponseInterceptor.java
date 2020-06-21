package com.arc.common.aspect;

import com.arc.common.annotation.NoWrapperResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoWrapperResponseInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (HandlerMethod.class.isInstance(handler)
                && ((HandlerMethod) handler).hasMethodAnnotation(NoWrapperResponse.class)) {
            response.setHeader("X-Disable-Wrap", "true");
        }
    }
}
