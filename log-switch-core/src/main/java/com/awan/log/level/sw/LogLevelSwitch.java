package com.awan.log.level.sw;

import com.awan.log.level.sw.constants.SwitchStatusEnum;
import com.awan.log.level.sw.context.LogContext;
import com.awan.log.level.sw.context.SwitchContext;
import com.awan.log.level.sw.listener.ChangeListenerFactory;
import com.awan.log.level.sw.listener.ConfigListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function: 
 *
 * @author awan
 * @date 2022/3/23
 */
public class LogLevelSwitch {
    private static final Logger LOG = LoggerFactory.getLogger(LogLevelSwitch.class);

    private LogContext logContext;

    private SwitchContext switchContext;

    /**
     * 初始化日志开关
     * - 构建logger map
     * - 保存配置状态
     * - off-保持不动，on-覆盖配置
     */
    public LogLevelSwitch() {
        logContext = new LogContext();
        ConfigListener configListener = ChangeListenerFactory.getListener();
        configListener.register(this);
        this.switchContext = configListener.getInitSwitch();
        if (this.switchContext == null) {
            LOG.warn("can not get init switch");
            return;
        }
        if (SwitchStatusEnum.OFF.equals(switchContext.getStatus())) {
            return;
        }
        logContext.setLogLevel(switchContext);
    }

    /**
     * 更新动作
     */
    public void notifyLogSwitch(SwitchContext newSwitchContext) {
        LOG.info("notify switch context");
        this.switchContext = newSwitchContext;
        this.logContext.setLogLevel(switchContext);
    }

}
