package com.awan.log.level.sw;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function: 
 *
 * @author awan
 * @date 2022/3/24
 */
public class LogLevelSwitchTest {
    private Logger logger = LoggerFactory.getLogger(LogLevelSwitchTest.class);

    @Test
    public void initTest() {
        LogLevelSwitch logLevelSwitch = new LogLevelSwitch();
    }

    @Test
    public void logChangeTest() {
        LogLevelSwitch logLevelSwitch = new LogLevelSwitch();
        for (int i = 0; i < 100 ; i++) {
            logger.info("this is info msg");
            logger.warn("this is warn msg");
            logger.error("this is error msg");
        }
    }
}
