package com.seig.rental.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 路由拦截配置
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // Knife4j 文档
                        "/doc.html", "/webjars/**", "/v3/api-docs/**", "/swagger-resources/**",
                        // 登录接口 (context-path已在application中配置，此处路径为相对路径)
                        "/login/**",
                        // 错误页面
                        "/error"
                );
    }
}
