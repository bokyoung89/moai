package com.bokyoung.moai.common.config;

import com.bokyoung.moai.common.security.UserDetailsServiceImpl;
import com.bokyoung.moai.staff.filter.JwtAuthorizationFilter;
import com.bokyoung.moai.staff.handler.JwtAuthenticationEntryPoint;
import com.bokyoung.moai.staff.handler.RoleAccessDeniedHandler;
import com.bokyoung.moai.staff.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleAccessDeniedHandler roleAccessDeniedHandler; // 권한 예외 처리
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Token 인증 예외 처리

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                                .antMatchers("/moai/staff/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(roleAccessDeniedHandler) // JWT Token은 있으나 접근 권한이 없는 경우
                .authenticationEntryPoint(jwtAuthenticationEntryPoint); // 유효하지 않은 Token으로 요청 또는 Token 없이 요청한 경우
        return http.build();
    };
}

