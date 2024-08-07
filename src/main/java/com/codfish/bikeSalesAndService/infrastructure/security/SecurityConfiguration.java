package com.codfish.bikeSalesAndService.infrastructure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    )
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    SecurityFilterChain securityEnabled(HttpSecurity http) throws Exception {
        http.csrf().disable();
        configureHttpRequests(http);
        configureExceptionHandling(http);
        configureLogin(http);
        configureLogout(http);

        return http.build();
    }

    private void configureHttpRequests(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(openAccessUrls()).permitAll()
                .requestMatchers(personRepairingUrls()).hasAnyAuthority("PERSON_REPAIRING")
                .requestMatchers(salesmanUrls()).hasAnyAuthority("SALESMAN")
                .requestMatchers(generalUrls()).hasAnyAuthority("PERSON_REPAIRING", "SALESMAN");
    }

    private void configureExceptionHandling(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    private void configureLogin(HttpSecurity http) throws Exception {
        http.formLogin().permitAll();
    }

    private void configureLogout(HttpSecurity http) throws Exception {
        http.logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    private String[] openAccessUrls() {
        return new String[]{"/login", "/error", "/images/**"};
    }

    private String[] personRepairingUrls() {
        return new String[]{"/personRepairing/**", "/add_update_parts/**", "/add_part/**", "/update_part/**", "/delete_part/**",
                "/add_update_services/**", "/add_service/**", "/update_service/**", "/delete_service**",
                "/add_update_person_repairing/**", "/add_person_repairing/**", "/update_person_repairing/**", "/delete_person_repairing/**"};
    }

    private String[] salesmanUrls() {
        return new String[]{"/salesman/**", "/purchase/**", "/purchase_new_customer/**", "/add_bike/**", "/update_bike/**",
                "/delete_bike/**", "/add_update_salesman/**",
                "/add_salesman/**", "/update_salesman/**", "/delete_salesman/**"};
    }

    private String[] generalUrls() {
        return new String[]{"/", "/bike/**", "/images/logo/logo2.png", "/images/error.png", "/service/**", "/customers_purchases/**",
                "/invoices_purchases/**", "/add_customer/**", "/update_customer/**", "/delete_customer/**",  "/user/info","/error",};
    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
    SecurityFilterChain securityDisabled(HttpSecurity http) throws Exception {
        configureHttpSecurity(http);
        return http.build();
    }

    private void configureHttpSecurity(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .permitAll();
    }
}