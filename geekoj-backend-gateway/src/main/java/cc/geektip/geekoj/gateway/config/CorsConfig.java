package cc.geektip.geekoj.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: CORS Config
 * @author: Fish
 * @date: 2024/3/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsConfig {
    private String maxAge;
    private String allowedOrigins;
    private String allowedMethods;
    private String allowedHeaders;
}