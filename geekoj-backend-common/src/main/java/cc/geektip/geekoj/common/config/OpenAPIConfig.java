package cc.geektip.geekoj.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Knife4j配置
 * @author: Bill Yu
 */
@Configuration
public class OpenAPIConfig {
    @Resource
    ApiDocProperties apiDocProperties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(apiDocProperties.getTitle())
                        .version(apiDocProperties.getVersion())
                        .description(apiDocProperties.getDescription())
                        .contact(new Contact().name(apiDocProperties.getContact().getName())
                                .url(apiDocProperties.getContact().getUrl()))
                        .license(new License().name(apiDocProperties.getLicense().getName())
                                .url(apiDocProperties.getLicense().getUrl())));
    }
}
