package com.awan.log.level.sw.listener;

import java.util.ServiceLoader;

/**
 * Function: 
 *
 * @author awan
 * @date 2022/3/24
 */
public class ChangeListenerFactory {
    public static ConfigListener getListener() {
        final ServiceLoader<ConfigListener> loader = ServiceLoader
                .load(ConfigListener.class);
        for (ConfigListener configListener : loader) {
            return configListener;
        }
        throw new IllegalArgumentException("please choose valid listener");
    }
}
