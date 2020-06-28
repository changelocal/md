/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arc.db.jsd.stats;

import com.alibaba.fastjson.JSON;
import com.arc.db.jsd.ConnectionManager;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

/**
 * 数据库操作日志上报管理器,减少NSQ的操作,将本地的操作日志打包进行上报,时间阈值为1妙,每次多余1000条记录后
 * 也进行上报,为单例模式.
 *
 * @author louis
 */
@Slf4j
public class StatsManager {

    /**
     * 执行sql的应用的主机
     */
    private final static String host;
    /**
     * 执行sql的appname
     */
    private final static String appName;

    /**
     * 是否上报数据库统计信息
     */
    private static final boolean statsReport;

    private static final String LOGGER_NAME = "kf.arc.db.jsd.$stats";
    private static String reportType;

    static {
        String sStatsReport = "";//ConfigProperties.getProperty("jsd.stats.enabled", "false");
        statsReport = false;//StringConverter.toBool(sStatsReport, false);
        reportType = "";//AppConfig.getDefault().getGlobal().getRpcClientStatsType();
        host="";
        appName="";
        /*if (!"nsq".equalsIgnoreCase(reportType) && LoggerManager.hasLogger(LOGGER_NAME)) {
            logger = LoggerManager.getLogger(LOGGER_NAME);
        }
        if (statsReport) {
            AppConfig appCfg = AppConfig.getDefault();
            GlobalConfig globalCfg = appCfg.getGlobal();
            host = globalCfg.getRpcRegisterIP();
            appName = appCfg.getAppName();
        } else {
            host = "";
            appName = "";
        }*/
    }
    
    public static final StatsManager instance = new StatsManager();

    /**
     * 保存业务统计日志的队列
     */
    private LinkedTransferQueue<DatabaseStats> queue = new LinkedTransferQueue<>();
    /**
     * 数据库连接管理器和数据库连接信息的缓存
     */
    private ConcurrentHashMap<DataSource, String> dbInfoCache = new ConcurrentHashMap<>();

    private StatsManager() {
        if (statsReport && "nsq".equals(reportType)) {
            //启动处理线程
            Runnable processor = new StatsSender(queue);
            Thread thread = new Thread(processor);
            thread.start();
        }
    }

    /**
     * 是否支持数据库统计功能
     * @return 
     */
    public boolean isReportEnabled() {
        return statsReport;
    }

    /**
     * 根据数据库连接管理器以及数据库连接获取数据库的连接信息,先查询缓存中的数据,如果缓存中没有
     * 则根据数据库连接进行获取
     * @param connManager 数据库连接管理器
     * @param conn 数据库连接
     * @return 数据库的连接信息
     */
    public String getJdbcUrl(ConnectionManager connManager, Connection conn) {
        if (connManager == null || connManager.getSource() == null) {
            return null;
        }

        String jdbcUrl = dbInfoCache.get(connManager.getSource());
        if (jdbcUrl == null) {
            jdbcUrl = StatsUtil.getJdbcUrl(conn);
            if (!Strings.isNullOrEmpty(jdbcUrl)) {
                String oldInfo = dbInfoCache.putIfAbsent(connManager.getSource(), jdbcUrl);
                if (oldInfo != null) {
                    jdbcUrl = oldInfo;
                }
            }
        }
        return jdbcUrl;
    }

    /**
     * 将数据库统计的日志添加到数据上报管理器中,添加到管理器的队列中
     *
     * @param status 数据操作的日志
     */
    public void addStats(DatabaseStats status) {
        if (status == null) {
            return;
        }
        status.setAppName(appName);
        status.setShost(host);
        if ("nsq".equals(reportType)) {
            queue.offer(status);
        } else if (log != null) {
            log.info(JSON.toJSONString(status, false));
        }
    }

    /**
     * 数据上报的线程,缓存中多余1000条或者时间超过1秒后进行上报
     */
    private class StatsSender implements Runnable {


        private LinkedTransferQueue<DatabaseStats> queue;
        private long lastReportTime = 0;
        private List<DatabaseStats> cache = new ArrayList<>();

        public StatsSender(LinkedTransferQueue<DatabaseStats> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            log.info("数据库统计上报处理器启动...");
            while (true) {
                DatabaseStats stats = null;
                try {
                    stats = queue.poll(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {
                    log.warn("get element error from queue", ex);
                }
                if (stats != null) {
                    cache.add(stats);
                }
                //当缓存的数据大于1000或者多余1秒时进行数据上报
                if (cache.size() >= 1000) {
                    reportData(cache);
                } else {
                    if (!cache.isEmpty()) {
                        long now = System.currentTimeMillis();
                        if ((now - lastReportTime) >= 1000) {
                            reportData(cache);
                            lastReportTime = now;
                        }
                    }
                }
            }
        }

        /**
         * 将需要上报的数据进行上报,上报后将缓存清空
         */
        private void reportData(List<DatabaseStats> cache) {
            try {
                //Publisher.get().publish("apm_mysql_log", (Object) cache);
            } catch (Exception e) {
                log.warn("数据上报失败", e);
            }
            cache.clear();
        }
    }
}
