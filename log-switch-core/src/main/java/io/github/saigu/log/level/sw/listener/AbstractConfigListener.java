package io.github.saigu.log.level.sw.listener;

import io.github.saigu.log.level.sw.LogLevelSwitch;
import io.github.saigu.log.level.sw.context.SwitchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function: 
 *
 * @author awan
 * @date 2022/3/27
 */
public abstract class AbstractConfigListener<T> implements ConfigListener<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractConfigListener.class);

    private LogLevelSwitch logLevelSwitch;

    /**
     * 注册回调对象
     * @param logLevelSwitch
     */
    @Override
    public void register(final LogLevelSwitch logLevelSwitch) {
        this.logLevelSwitch = logLevelSwitch;
    }

    /**
     * 最简单的处理。
     * - 接收监听配置
     * - 转化配置
     * - 回调logLevelSwitch
     * @param changedConfig
     */
    @Override
    public void listenChangedConfig(final T changedConfig) {
        SwitchContext newConfig = transferConfig(changedConfig);
        onConfigChange(newConfig);
    }

    /**
     * 监听switch变化
     * @param newConfig    发生变更数据
     */
    @Override
    public void onConfigChange(final SwitchContext newConfig) {
        if (newConfig == null) {
            throw new IllegalArgumentException("receive switch change empty");
        }
        LOG.info("receive switch change: " + newConfig.toString());
        this.logLevelSwitch.notifyLogSwitch(newConfig);
    }
}
