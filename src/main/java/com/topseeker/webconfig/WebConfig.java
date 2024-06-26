package com.topseeker.webconfig;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.topseeker.filter.MemLoginFilter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<MemLoginFilter> loginFilter() {
        FilterRegistrationBean<MemLoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MemLoginFilter());
        registrationBean.addUrlPatterns("/protected/*"); // 添加需要过滤的 URL 模式
        return registrationBean;
    }
}
