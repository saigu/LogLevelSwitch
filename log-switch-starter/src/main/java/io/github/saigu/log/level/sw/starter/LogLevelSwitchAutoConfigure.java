package io.github.saigu.log.level.sw.starter;

import io.github.saigu.log.level.sw.LogLevelSwitch;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Function: 
 *
 * @author awan
 * @date 2022/3/30
 */
@Configuration
@EnableConfigurationProperties(LogLevelSwitchAutoConfigure.class)
public class LogLevelSwitchAutoConfigure {

    @Bean
    LogLevelSwitch logLevelSwitch() {
        return new LogLevelSwitch();
    }
}
