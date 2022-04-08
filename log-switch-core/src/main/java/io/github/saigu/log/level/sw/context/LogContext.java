package io.github.saigu.log.level.sw.context;

import io.github.saigu.log.level.sw.constants.LogConstant;
import io.github.saigu.log.level.sw.constants.LogFrameworkType;
import io.github.saigu.log.level.sw.constants.SwitchStatusEnum;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticLoggerBinder;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Function: 
 *
 * @author awan
 */
public class LogContext {
    private final Logger LOG = LoggerFactory.getLogger(LogContext.class);
    private LogFrameworkType logFrameworkType = LogFrameworkType.LOG4J2;
    private final Map<String, Object> loggerMap = new ConcurrentHashMap<>(2);
    private final Map<String, String> loggerLevelBck = new ConcurrentHashMap<>(2);

    private static final String ROOT_LOG_KEY = "root";
    private static final String STATIC_LOGGER_BINDER_PATH =
            "org/slf4j/impl/StaticLoggerBinder.class";

    public LogContext() {
        logCheck();
        buildLogMap();
    }

    /**
     * 日志框架使用检查，规范使用
     */
    private void logCheck() {
        Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
        if (staticLoggerBinderPathSet.isEmpty()) {
            LOG.error("缺少日志实现，建议引入日志依赖logback或者log4j2");
        } else if (staticLoggerBinderPathSet.size() > 1) {
            LOG.warn("存在多个日志框架实现，建议只保留一个，否则会影响日志降级开关效果");
        }
    }


    /**
     * 初始化：确定所使用的日志框架，获取配置文件中所有的Logger内存实例，并将它们的引用缓存到Map容器中。
     */
    public void buildLogMap() {
        String type = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
        if (LogConstant.LOG4J_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOG4J;
            Enumeration enumeration = org.apache.log4j.LogManager.getCurrentLoggers();
            while (enumeration.hasMoreElements()) {
                org.apache.log4j.Logger logger =
                        (org.apache.log4j.Logger) enumeration.nextElement();
                if (logger.getLevel() != null) {
                    loggerMap.put(logger.getName(), logger);
                    loggerLevelBck.put(logger.getName(), logger.getLevel().toString());
                }
            }
            org.apache.log4j.Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
            loggerMap.put(ROOT_LOG_KEY, rootLogger);
            loggerLevelBck.put(ROOT_LOG_KEY, rootLogger.getLevel().toString());
        } else if (LogConstant.LOGBACK_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOGBACK;
            ch.qos.logback.classic.LoggerContext loggerContext =
                    (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
            for (ch.qos.logback.classic.Logger logger : loggerContext.getLoggerList()) {
                if (logger.getLevel() != null) {
                    loggerMap.put(logger.getName(), logger);
                    loggerLevelBck.put(logger.getName(), logger.getLevel().toString());
                }
            }
            ch.qos.logback.classic.Logger rootLogger =
                    (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(
                            Logger.ROOT_LOGGER_NAME);
            loggerMap.put(ROOT_LOG_KEY, rootLogger);
            loggerLevelBck.put(ROOT_LOG_KEY, rootLogger.getLevel().toString());
        } else if (LogConstant.LOG4J2_LOGGER_FACTORY.equals(type)) {
            logFrameworkType = LogFrameworkType.LOG4J2;
            org.apache.logging.log4j.core.LoggerContext loggerContext =
                    (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager
                            .getContext(false);
            Map<String, LoggerConfig> map = loggerContext.getConfiguration().getLoggers();
            for (org.apache.logging.log4j.core.config.LoggerConfig loggerConfig : map.values()) {
                String key = loggerConfig.getName();
                if (key == null || key.isEmpty()) {
                    key = ROOT_LOG_KEY;
                }
                loggerMap.put(key, loggerConfig);
                loggerLevelBck.put(key, loggerConfig.getLevel().toString());
            }
        } else {
            logFrameworkType = LogFrameworkType.UNKNOWN;
            LOG.error("Log框架无法识别: type={}", type);
        }
    }


    /**
     * 修改Logger的级别
     * @param switchContext
     * - off-恢复状态
     * - on-自定义修改（all全局修改）
     * @return
     */
    public boolean setLogLevel(final SwitchContext switchContext) {
        if (switchContext == null) {
            LOG.warn("can not get switch context");
            return false;
        }
        List<LoggerBean> loggerList = switchContext.getLoggerBeans();
        SwitchStatusEnum switchStatus = switchContext.getStatus();
        if (SwitchStatusEnum.OFF.equals(switchStatus)) {
            for (Map.Entry<String, Object> entry : loggerMap.entrySet()) {
                Object logger = entry.getValue();
                String bckLevel = loggerLevelBck.get(entry.getKey());
                if (bckLevel != null) {
                    setLogLevelInternal(logger, bckLevel);
                }
            }
            return true;
        }
        LoggerBean loggerBean0 = loggerList.get(0);
        if (LogConstant.ALL_LOGGER.equalsIgnoreCase(loggerBean0.getName())) {
            for (Map.Entry<String, Object> entry : loggerMap.entrySet()) {
                Object logger = entry.getValue();
                setLogLevelInternal(logger, loggerBean0.getLevel());
            }
            return true;
        }
        for (LoggerBean loggerbean : loggerList) {
            Object logger = loggerMap.get(loggerbean.getName());
            if (logger == null) {
                LOG.error("需要修改日志级别的Logger不存在");
                return false;
            }
            setLogLevelInternal(logger, loggerbean.getLevel());
        }
        return true;
    }

    private void setLogLevelInternal(Object logger, String level) {
        if (logger == null || level == null) {
            return;
        }
        if (logFrameworkType == LogFrameworkType.LOG4J) {
            org.apache.log4j.Logger targetLogger = (org.apache.log4j.Logger) logger;
            org.apache.log4j.Level targetLevel = org.apache.log4j.Level.toLevel(level);
            targetLogger.setLevel(targetLevel);
        } else if (logFrameworkType == LogFrameworkType.LOGBACK) {
            ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) logger;
            ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(level);
            targetLogger.setLevel(targetLevel);
        } else if (logFrameworkType == LogFrameworkType.LOG4J2) {
            org.apache.logging.log4j.core.config.LoggerConfig loggerConfig =
                    (org.apache.logging.log4j.core.config.LoggerConfig) logger;
            org.apache.logging.log4j.Level targetLevel = org.apache.logging.log4j.Level.toLevel(
                    level);
            loggerConfig.setLevel(targetLevel);
            org.apache.logging.log4j.core.LoggerContext ctx =
                    (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager
                            .getContext(false);
            ctx.updateLoggers(); // This causes all Loggers to refetch information from their LoggerConfig.
        } else {
            LOG.error("Logger的类型未知,无法处理!");
        }
    }

    /**
     * copy from org.slf4j.LoggerFactory
     * @return
     */
    static Set<URL> findPossibleStaticLoggerBinderPathSet() {
        LinkedHashSet staticLoggerBinderPathSet = new LinkedHashSet();

        try {
            ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
            Enumeration paths;
            if (loggerFactoryClassLoader == null) {
                paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
            } else {
                paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            }

            while (paths.hasMoreElements()) {
                URL path = (URL) paths.nextElement();
                staticLoggerBinderPathSet.add(path);
            }
        } catch (IOException var4) {
            Util.report("Error getting resources from path", var4);
        }

        return staticLoggerBinderPathSet;
    }
}
