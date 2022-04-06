package com.awan.log.level.sw.listener;

import com.awan.log.level.sw.LogLevelSwitch;
import com.awan.log.level.sw.context.SwitchContext;

/**
 * Function: 
 *
 * @author awan
 * @date 2022/3/20
 */
public interface ConfigListener<T> {

    /**
     * 注册logLevelSwitch用于回调
     * @param logLevelSwitch
     */
    void register(LogLevelSwitch logLevelSwitch);

    /**
     * 获取初始开关状态
     * @return
     */
    SwitchContext getInitSwitch();

    /**
     * 获取变化的配置
     * @param changedConfig
     */
    void listenChangedConfig(T changedConfig);

    /**
     * 配置转化为SwitchContext
     * @param changedConfig
     * @return
     */
    SwitchContext transferConfig(T changedConfig);

    /**
     * 处理变更逻辑
     * @param newConfig    发生变更数据
     */
    void onConfigChange(SwitchContext newConfig);
}
