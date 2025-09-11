package com.wtx.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保静态资源可以被访问
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // 添加默认的静态资源路径映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
