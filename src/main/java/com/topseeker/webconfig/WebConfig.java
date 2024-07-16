package com.topseeker.webconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
=======
>>>>>>> refs/heads/master

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
  
<<<<<<< HEAD
//  @Override
//  public void addFormatters(FormatterRegistry registry) {
//      registry.addConverter(new MultipartFileToArtPicVOConverter());
//  }
=======
>>>>>>> refs/heads/master
}
