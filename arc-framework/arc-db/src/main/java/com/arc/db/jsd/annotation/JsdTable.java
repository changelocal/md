package com.arc.db.jsd.annotation;

import com.arc.db.jsd.NameStyle;

import java.lang.annotation.*;


@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsdTable {
    /**
     * 表字段命名风格
     * @return
     */
    NameStyle nameStyle() default NameStyle.PASCAL;

    /**
     * 表分片字段
     * @return
     */
    String[] shardKeys() default {};
}
