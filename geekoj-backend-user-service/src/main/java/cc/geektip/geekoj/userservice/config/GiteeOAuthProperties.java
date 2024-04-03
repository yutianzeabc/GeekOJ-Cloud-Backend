package cc.geektip.geekoj.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Gitee OAuth 配置
 * @author: Fish
 * @date: 2024/4/1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "geekoj.third-party.oauth.gitee")
public class GiteeOAuthProperties {
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String redirectUri;
}
