package org.pztsblog.blogsend.config;

import org.pztsblog.blogsend.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**") // 拦截所有 /api 开头的请求
                .excludePathPatterns(       // 排除以下白名单（不需要登录就能访问的接口）
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/articles/list",  // 获取文章列表
                        "/api/articles/{id}",  // 获取文章详情
                        "/api/comments/article/**" // 获取文章的评论列表
                );
    }
}