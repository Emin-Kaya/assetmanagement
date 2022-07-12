package com.bht.assetmanagement.config;

import com.bht.assetmanagement.config.security.JwtAuthorizationFilter;
import com.bht.assetmanagement.config.security.JwtUtils;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList(RequestMethod.POST.name(),
                RequestMethod.GET.name(),
                RequestMethod.PUT.name(),
                RequestMethod.DELETE.name(),
                RequestMethod.PATCH.name(),
                RequestMethod.OPTIONS.name()));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/testdata/**").permitAll()
                .antMatchers("/api/v1/applicationUser/**").permitAll()
                .antMatchers("/api/v1/employee/asset/**").hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_MANAGER")
                .antMatchers("/api/v1/asset/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                .antMatchers("/api/v1/storage/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                .antMatchers("/api/v1/assetInquiry/**").hasAnyAuthority("ROLE_EMPLOYEE")
                .antMatchers("/api/v1/manager/assetInquiry/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                .antMatchers("/api/v1/admin/userAccount/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/api/v1/admin/applicationUser/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();
        http.headers().frameOptions().sameOrigin();
        http.addFilterBefore(new JwtAuthorizationFilter(userAccountService, jwtUtils), UsernamePasswordAuthenticationFilter.class);
    }
}
