package io.github.saigu.log.level.sw.context;

import io.github.saigu.log.level.sw.constants.SwitchStatusEnum;
import io.github.saigu.log.level.sw.util.SwitchObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Function:
 *
 * @author awan
 * @date 2022/3/24
 */
public class SwitchContext {
    private SwitchStatusEnum status;

    private List<LoggerBean> loggerBeans;

    public SwitchStatusEnum getStatus() {
        return status;
    }

    public void setStatus(final SwitchStatusEnum status) {
        this.status = status;
    }

    public List<LoggerBean> getLoggerBeans() {
        return loggerBeans;
    }

    public void setLoggerBeans(final List<LoggerBean> loggerBeans) {
        this.loggerBeans = loggerBeans;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = SwitchObjectMapperHolder.getSwitchObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "error json string";
        }
    }
}
