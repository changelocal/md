package com.arc.db.jsd;


import com.arc.db.jsd.result.BuildResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by noname on 15/11/19.
 */
@Slf4j
public final class Debug {
    private final static String LOGGER_NAME = "kf.arc.db.jsd.debug";
    /*private static Logger LOGGER = LoggerManager.getLogger(LOGGER_NAME);
    private static boolean ENABLED = LoggerManager.hasLogger(LOGGER_NAME);*/

    static void log(BuildResult result) {
        if (log.isDebugEnabled()) {
            log.debug("sql: {}, args: {}", result.getSql(), result.getArgs());
        }
    }

    static void log(String sql, Object[] args) {
        if (log.isDebugEnabled()) {
            log.debug("sql: {}, args: {}", sql, args);
        }
    }
}
