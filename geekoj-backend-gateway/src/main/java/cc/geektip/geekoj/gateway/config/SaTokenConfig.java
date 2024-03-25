package cc.geektip.geekoj.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.reactor.spring.SaTokenContextForSpringReactor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.spring.pathmatch.SaPatternsRequestConditionHolder;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @description: Sa-Token 配置类
 * @author: Fish
 * @date: 2024/3/17
 */
@Configuration
public class SaTokenConfig {
    @Resource
    private CorsConfig CorsConfig;

    /**
     * 注册 Sa-Token 全局鉴权过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 开放地址
                .addExclude(
                        "/doc.html",
                        "/doc.html*",
                        "/webjars/**",
                        "/img.icons/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/**/v3/api-docs"
                )
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    SaRouter.match("/**", "/api/user/login", r -> StpUtil.checkLogin());
                })
                // 设置跨域 必须
                .setBeforeAuth(obj -> {
                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", CorsConfig.getAllowedOrigins())
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", CorsConfig.getAllowedMethods())
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", CorsConfig.getMaxAge())
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", CorsConfig.getAllowedHeaders())
                            .setHeader("X-XSS-Protection", "1; mode=block");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS).back();
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> SaResult.error(e.getMessage()));
    }

    /**
     * 自定义 SaTokenContext 实现类，重写 matchPath 方法，切换为 ant_path_matcher 模式，使之可以支持 `**` 之后再出现内容
     */
    @Primary
    @Component
    public static class SaTokenContextByPatternsRequestCondition extends SaTokenContextForSpringReactor {
        @Override
        public boolean matchPath(String pattern, String path) {
            return SaPatternsRequestConditionHolder.match(pattern, path);
        }

    }
}
