package com.example.assetmanagement.config;

import com.example.assetmanagement.exception.UnauthorizedHandler;
import com.example.assetmanagement.security.JwtAuthorizationFilter;
import com.example.assetmanagement.security.JwtUtils;
import com.example.assetmanagement.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;
    private final UnauthorizedHandler unauthorizedHandler;
    private final JwtUtils jwtUtils;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userAccountService).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/v1/applicationUser/**").hasAnyAuthority("ROLE_EMPLOYEE")
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new JwtAuthorizationFilter(userAccountService, jwtUtils), UsernamePasswordAuthenticationFilter.class);
    }
}
