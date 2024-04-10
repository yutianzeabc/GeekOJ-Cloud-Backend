package cc.geektip.geekoj.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: System Config
 * @author: Bill Yu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system.security")
public class SystemProperties {
    /**
     * 是否开启禁用API检查
     */
    private boolean checkDisabledApis;
    /**
     * 无需鉴权的API
     */
    private String[] noAuthApis;
    /**
     * 禁用的API
     */
    private String[] disabledApis;
}
