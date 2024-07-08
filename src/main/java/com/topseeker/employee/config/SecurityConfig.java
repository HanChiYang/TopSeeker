package com.topseeker.employee.config;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .antMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
//                .antMatchers("/back-end/emp/**").authenticated()
//                .anyRequest().permitAll()
//            .and()
//            .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/back-end/emp/select_page")
//                .permitAll()
//            .and()
//            .logout()
//                .permitAll();
//    }
//}