package cc.geektip.geekoj.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: Fish
 * @date: 2024/3/17
 */
@Configuration
public class SaTokenConfig {

    /**
     * 注册 Sa-Token 全局鉴权过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 开放地址
                .addExclude("/favicon.ico", "/doc.html")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    SaRouter.match("/**", "/api/user/login", r -> StpUtil.checkLogin());
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> SaResult.error(e.getMessage()));
    }
}
