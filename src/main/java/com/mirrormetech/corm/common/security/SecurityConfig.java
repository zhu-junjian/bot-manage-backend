package com.mirrormetech.corm.common.security;

import com.mirrormetech.corm.common.security.filter.AuthFilter;
import com.mirrormetech.corm.common.security.filter.LoginFilter;
import com.mirrormetech.corm.common.security.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security 认证链
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    private final AuthFilter authFilter;

    public SecurityConfig(JwtProvider jwtProvider, AuthFilter authFilter) {
        this.jwtProvider = jwtProvider;
        this.authFilter = authFilter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 指定某些接口不需要通过验证即可访问。像登陆、注册接口肯定是不需要认证的
                        .requestMatchers("/api/*/auth/**","/api/*/auth/**","/mqttBackend/**").permitAll()
                        // 这里意思是其它所有接口需要认证才能访问
                        .requestMatchers("/api/**").authenticated()
                        // 后台管理接口只有管理员权限账号可以访问
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new MyEntryPoint())
                        .accessDeniedHandler(new MyDeniedHandler())
                )
                .addFilterBefore(LoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authFilter, AuthorizationFilter.class);
        return http.build();
    }

    /**
     * 关键配置：暴露AuthenticationManager为Bean
     * @param authConfig
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public LoginFilter LoginFilter() {
        return new LoginFilter(jwtProvider);
    }
}
