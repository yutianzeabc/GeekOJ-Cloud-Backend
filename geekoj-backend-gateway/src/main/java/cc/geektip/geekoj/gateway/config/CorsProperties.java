package cc.geektip.geekoj.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: CORS Config
 * @author: Bill Yu
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String maxAge;
    private String allowedOrigin;
    private String allowedMethods;
    private String allowedHeaders;
}
