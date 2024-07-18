package com.topseeker.webconfig;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;

import com.topseeker.filter.MemLoginFilter;
import com.topseeker.shop.sale.model.DateToTimestampConverter;

@Configuration
public class WebConfig {

  @Bean
  public FilterRegistrationBean<MemLoginFilter> loginFilter() {
  FilterRegistrationBean<MemLoginFilter> registrationBean = new FilterRegistrationBean<>();
  registrationBean.setFilter(new MemLoginFilter());

  registrationBean.addUrlPatterns("/protected/*");
  return registrationBean;
  
    }
}
