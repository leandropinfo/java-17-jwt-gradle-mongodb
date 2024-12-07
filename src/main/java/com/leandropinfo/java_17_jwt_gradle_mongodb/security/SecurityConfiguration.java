package com.leandropinfo.java_17_jwt_gradle_mongodb.security;

import com.leandropinfo.java_17_jwt_gradle_mongodb.enums.NivelacessoEnum;
import com.leandropinfo.java_17_jwt_gradle_mongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    private AuthenticationConfiguration authenticationConfiguration;

	private JwtTokenUtil jwtUtil;

    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    public SecurityConfiguration(AuthenticationConfiguration authenticationConfiguration, JwtTokenUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {

        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> {
                auth.requestMatchers("/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/adm/**").hasAuthority(NivelacessoEnum.ROLE_ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.POST, "/adm/**").hasAuthority(NivelacessoEnum.ROLE_ADMINISTRATOR.toString())
                        .anyRequest()
                        .authenticated();
        });

        http.addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), jwtUtil, userService));
		http.addFilter(new JWTAuthorizationFilter(authenticationConfiguration.getAuthenticationManager(), jwtUtil, userService, userDetailsService));
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
       return http.build();
    }


}