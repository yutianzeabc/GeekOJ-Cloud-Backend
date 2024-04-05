package cc.geektip.geekoj.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.reactor.spring.SaTokenContextForSpringReactor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.spring.pathmatch.SaPatternsRequestConditionHolder;
import cn.dev33.satoken.util.SaResult;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @description: Sa-Token 配置类
 * @author: Fish
 *
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
                // 设置跨域 必须
                .setBeforeAuth(obj -> {
                    var request = SaHolder.getRequest();
                    var response = SaHolder.getResponse();
                    response.setHeader("Access-Control-Allow-Credentials", "true")
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", CorsConfig.getAllowedOrigins())
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", CorsConfig.getAllowedMethods())
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", CorsConfig.getMaxAge())
                            // 允许携带cookie
                            .setHeader("Access-Control-Allow-Headers", CorsConfig.getAllowedHeaders())
                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block");
                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS).back();
                })
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除部分特殊路由
//                    SaRouter.match("/api/user/**").notMatch("/api/user/register", "/api/user/login", "/api/user/get/login").check(r -> StpUtil.checkLogin());
//                    SaRouter.match("/api/question/**").notMatch("/api/question/list/page/vo", "/api/question/question_submit/list/page").check(r -> StpUtil.checkLogin());
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
