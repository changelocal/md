package com.arc.util.config;

/**
 * Created by xiuquan.tian on 15/4/27.
 */
public class ConfigException extends RuntimeException {
    public ConfigException() {
        super();
    }

    public ConfigException(String msg) {
        super(msg);
    }

    public ConfigException(Exception inner) {
        super(inner);
    }

    public ConfigException(String msg, Exception inner) {
        super(msg, inner);
    }
}
