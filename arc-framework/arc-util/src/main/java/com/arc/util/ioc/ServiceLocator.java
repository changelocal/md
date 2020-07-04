package com.arc.util.ioc;



/**
 * 定义通用 ServiceLocator 模式接口。
 */
public interface ServiceLocator {

    Object getInstance(String name);

    <T> T getInstance(Class<T> serviceType);

    <T> T getInstance(String name, Class<T> serviceType);

    public static ServiceLocator current() {
        if(!isCurrentSet()){
            throw new RuntimeException("current in.erp.data.service locator is not set.");
        }
        return ServiceLocatorHolder.current;
    }

    /**
     * 设置当前 ServiceLocator 实例。
     * @param serviceLocator
     */
    public static void setCurrent(ServiceLocator serviceLocator) {
        ServiceLocatorHolder.current = serviceLocator;
    }

    public static boolean isCurrentSet()
    {
        return  ServiceLocatorHolder.current != null;
    }

    /**
     * only due to static field in interfaces is final by default.
     */
    static class ServiceLocatorHolder {
        static ServiceLocator current = null;
    }
}
