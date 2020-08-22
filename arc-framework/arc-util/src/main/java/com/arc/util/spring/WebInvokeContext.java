package com.arc.util.spring;



import com.arc.util.context.InvokeContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 基于 Spring RequestContextHolder 实现的调用上下文容器。
 */
@Slf4j
public final class WebInvokeContext implements InvokeContext {


    private static final WebInvokeContext singleton = new WebInvokeContext();

    public static InvokeContext getInstance() {
        return singleton;
    }

    private WebInvokeContext() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String name) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request != null) {
            return (T) request.getAttribute(name);
        } else {
            log.warn("HttpServletRequest null, not spring web?");
            return null;
        }
    }

    @Override
    public <T> void set(String name, T value) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request != null) {
            request.setAttribute(name, value);
        } else {
            log.warn("HttpServletRequest null, not spring web?");
        }
    }
}
