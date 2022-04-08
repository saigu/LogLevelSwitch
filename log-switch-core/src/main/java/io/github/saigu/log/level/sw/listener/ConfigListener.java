package io.github.saigu.log.level.sw.listener;

import io.github.saigu.log.level.sw.LogLevelSwitch;
import io.github.saigu.log.level.sw.context.SwitchContext;

/**
 * Function: 
 *
 * @author awan
 */
public interface ConfigListener<T> {

    /**
     * 注册logLevelSwitch用于回调
     * @param logLevelSwitch callback object
     */
    void register(LogLevelSwitch logLevelSwitch);

    /**
     * 获取初始开关状态
     * @return initial context of switch
     */
    SwitchContext getInitSwitch();

    /**
     * 获取变化的配置
     * @param changedConfig changed config context
     */
    void listenChangedConfig(T changedConfig);

    /**
     * 配置转化为SwitchContext
     * @param changedConfig changed config context
     * @return switchContext format
     */
    SwitchContext transferConfig(T changedConfig);

    /**
     * 处理变更逻辑
     * @param newConfig  changed config context with switchContext format
     */
    void onConfigChange(SwitchContext newConfig);
}
