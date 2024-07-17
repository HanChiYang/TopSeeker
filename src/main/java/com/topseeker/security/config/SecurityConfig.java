//package com.topseeker.security.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeRequests(auth -> auth
//                        .antMatchers("/").permitAll()
//                        .antMatchers("/contact").permitAll()
//                        .antMatchers("/store/**").permitAll()
//                        .antMatchers("/register").permitAll()
//                        .antMatchers("/login").permitAll()
//                        .antMatchers("/logout").permitAll()
//                        .antMatchers("/admin/**").hasRole("ADMIN")
//                        .antMatchers("/store/**").hasRole("STORE_ADMIN")
//                        .antMatchers("/group/**").hasRole("GROUP_ADMIN")
//                        .antMatchers("/package/**").hasRole("PACKAGE_ADMIN")
//                        .antMatchers("/community/**").hasRole("COMMUNITY_ADMIN")
//                        .antMatchers("/member/**").hasRole("MEMBER_ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .failureUrl("/login?error=true")
//                        .defaultSuccessUrl("/backend_protected/backEndIndex", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout") // 登出路徑
//                        .logoutSuccessUrl("/login") // 登出成功後的重定向路徑
//                        .invalidateHttpSession(true) // 使 session 無效
//                        .deleteCookies("JSESSIONID") // 刪除 cookies
//                )
//                .build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
