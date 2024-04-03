package cc.geektip.geekoj.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: 线程池配置类
 *
 */
@Component
@ConfigurationProperties(prefix = "geekoj.thread-pool")
@Data
public class ThreadPoolProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}