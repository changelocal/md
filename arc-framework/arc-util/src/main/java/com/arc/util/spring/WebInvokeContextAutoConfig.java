package com.arc.util.spring;

import com.arc.util.context.InvokeContext;
import com.arc.util.context.InvokeContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication
public class WebInvokeContextAutoConfig {

    @PostConstruct
    private void init() {
        InvokeContextHolder.set(invokeContext());
    }

    @Bean(name = "arcInvokeContext")
    @ConditionalOnMissingBean(InvokeContext.class)
    public InvokeContext invokeContext() {
        return WebInvokeContext.getInstance();
    }
}
