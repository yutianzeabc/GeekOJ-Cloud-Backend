package cc.geektip.geekoj.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: Sa-Token 配置类，配置一些全局的 Sa-Token 事务处理器，如拦截器、过滤器等
 * @author: Fish
 * @date: 2024/3/17
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册 Sa-Token全局过滤器，解决跨域问题
     */
//    @Bean
//    public SaFilter getSaFilter() {
//        return new SaServletFilter()
//                // 拦截与排除 path
//                .addInclude("/**").addExclude("/favicon.ico", "doc.html")
//                // 全局认证函数
//                .setAuth(obj -> {
//                    // 校验 Id-Token 身份凭证
//                    SaSameUtil.checkCurrentRequestToken();
//                }).setError(e -> SaResult.error(e.getMessage()));
//    }
}