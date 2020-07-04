/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arc.db.jsd.stats;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @author louis
 */
@Getter
@Setter
public class DatabaseStats {
    /**
     * 数据库连接URL字符串
     */
    private String jdbcUrl;
    /**
     * 执行sql操作的数据库host
     */
    private String shost;
    /**
     * 执行数据库的appname
     */
    private String appName;
    /**
     * 执行的sql字符串
     */
    private String sql;
    /**
     * 执行sql时绑定的变量列表
     */
    private List<Object> args;
    /**
     * 是否执行成功
     */
    private boolean isSuccess;
    /**
     * 错误信息
     */
    private String error;
    /**
     * 执行sql的时间
     */
    private long time;
    /**
     * 执行sql的耗时ms
     */
    private int span;
    /**
     * 影响的行数
     */
    private int rows;
}
