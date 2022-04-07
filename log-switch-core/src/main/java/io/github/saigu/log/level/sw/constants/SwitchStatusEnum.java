package io.github.saigu.log.level.sw.constants;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Function: 
 *
 * @author awan
 * @date 2022/3/23
 */
public enum SwitchStatusEnum {
    // 0 开启， 1关闭
    @JsonProperty("on")
    ON(0),
    @JsonProperty("off")
    OFF(1);

    private final Integer value;

    SwitchStatusEnum(final int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
