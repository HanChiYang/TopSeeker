package com.topseeker.webconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.topseeker.filter.MemLoginFilter;
import com.topseeker.filter.NotificationFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public FilterRegistrationBean<MemLoginFilter> loginFilter() {
  FilterRegistrationBean<MemLoginFilter> registrationBean = new FilterRegistrationBean<>();
  registrationBean.setFilter(new MemLoginFilter());
  registrationBean.addUrlPatterns("/protected/*");
  return registrationBean;
  
    }
  
}
