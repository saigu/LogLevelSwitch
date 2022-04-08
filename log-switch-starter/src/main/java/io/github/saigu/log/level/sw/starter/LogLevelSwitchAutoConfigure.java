package io.github.saigu.log.level.sw.starter;

import io.github.saigu.log.level.sw.LogLevelSwitch;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Function: 
 *
 * @author awan
 */
@Configuration
@EnableConfigurationProperties(LogLevelSwitchAutoConfigure.class)
public class LogLevelSwitchAutoConfigure {

    @Bean
    LogLevelSwitch logLevelSwitch() {
        return new LogLevelSwitch();
    }
}
