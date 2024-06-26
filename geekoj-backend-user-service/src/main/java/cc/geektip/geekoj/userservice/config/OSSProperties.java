package cc.geektip.geekoj.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: OSS 配置类
 * @author: Bill Yu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "geekoj.third-party.oss")
public class OSSProperties {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;
}
