package io.github.saigu.log.level.sw.listener;

import java.util.ServiceLoader;

/**
 * Function: 
 *
 * @author awan
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
