package com.topseeker.webconfig;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.topseeker.filter.MemLoginFilter;
import com.topseeker.shop.sale.model.DateToTimestampConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public FilterRegistrationBean<MemLoginFilter> loginFilter() {
  FilterRegistrationBean<MemLoginFilter> registrationBean = new FilterRegistrationBean<>();
  registrationBean.setFilter(new MemLoginFilter());

  registrationBean.addUrlPatterns("/protected/*");
  return registrationBean;
  
    }
  
//  @Override
//  public void addFormatters(FormatterRegistry registry) {
//      registry.addConverter(new MultipartFileToArtPicVOConverter());
//  }
}
