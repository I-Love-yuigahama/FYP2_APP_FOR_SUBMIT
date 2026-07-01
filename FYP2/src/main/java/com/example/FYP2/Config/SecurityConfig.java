package com.example.FYP2.Config;

import com.example.FYP2.Service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Autowired
    private RateLimitFilter rateLimitFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
            httpSecurity.csrf(AbstractHttpConfigurer::disable)
                    .cors(cors -> cors.configurationSource(corsConfigurationSource))

                    // Security Headers
                    .headers(headers -> headers
                            .frameOptions(frame -> frame.deny()) // Prevent clickjacking
                            .contentTypeOptions(Customizer.withDefaults()) // X-Content-Type-Options: nosniff
                            .httpStrictTransportSecurity(hsts -> hsts
                                    .includeSubDomains(true)
                                    .maxAgeInSeconds(31536000)) // HSTS: 1 year
                            .referrerPolicy(referrer -> referrer
                                    .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                            .permissionsPolicy(permissions -> permissions
                                    .policy("camera=(), microphone=(), geolocation=()"))
                    )

                    .authorizeHttpRequests((requset -> requset.requestMatchers(
                            "/auth/**"
                                    ,"/public/**"
                                    ,"/error"
                                    ,"/ws/**"
                                    ,"/forgotPassword/**"
                                    ,"/error")
                            .permitAll()

                            .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                            .requestMatchers("/user/**").hasAnyAuthority("USER")
                            .requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN","USER")
                            .anyRequest().authenticated()))
                    .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class) // Rate limiting first
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider  authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;

    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
