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
System.out.println("webconfig");

  registrationBean.addUrlPatterns("/protected/*"); // 添加需要过滤的 URL 模式
  return registrationBean;
	
	
	
	
//    @Bean
//    public FilterRegistrationBean loginFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new MemLoginFilter());
//        
//        registrationBean.setUrlPatterns(Collections.singletonList("/protected/*"));
//        System.out.println("webconfig");
//        return registrationBean;
        
//        registrationBean.addUrlPatterns("/protected/*"); // 添加需要过滤的 URL 模式
//        return registrationBean;
    }
}
