package com.arc.util.lang;

import java.lang.reflect.Field;

/**
 * Created by xiuquan.tian on 2016/11/28.
 */
public final class Beans {
    public static String getGetterName(Field f) {
        String getter = f.getName();
        if (f.getType() != boolean.class) {
            getter = "get" + StrKit.firstCharToUpperCase(getter);
        } else if (!startWithIs(getter)) {
            getter = "is" + StrKit.firstCharToUpperCase(getter);
        }
        return getter;
    }

    public static String getSetterName(Field f) {
        String name = f.getName();
        if (f.getType() == boolean.class && startWithIs(name)) {
            name = name.substring(2);
        }
        return "set" + StrKit.firstCharToUpperCase(name);
    }

    private static boolean startWithIs(String name) {
        return name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2));
    }
}
