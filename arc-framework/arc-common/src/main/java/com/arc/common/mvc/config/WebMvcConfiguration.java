package com.arc.common.mvc.config;

import com.arc.common.aspect.NoWrapperResponseInterceptor;
import com.arc.common.aspect.ReceiveRequestOnlyInterceptor;
import com.arc.common.mvc.handler.HandlerEx4RespWrapResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NoWrapperResponseInterceptor());
        registry.addInterceptor(new ReceiveRequestOnlyInterceptor());
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new HandlerEx4RespWrapResolver());
    }
}
