package com.ssafy.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.security.auth.JwtAuthenticationFilter;
import com.ssafy.security.auth.JwtProvider;
import com.ssafy.security.auth.MemberDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final JwtProvider jwtProvider;
	private final MemberDetailsService memberDetailsService;
	
	public SecurityConfig(JwtProvider jwtProvider, MemberDetailsService memberDetailsService) {
		this.jwtProvider = jwtProvider;
		this.memberDetailsService = memberDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.authorizeRequests()
			.antMatchers("/member/join", "/member/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider, memberDetailsService), UsernamePasswordAuthenticationFilter.class);
			
		return http.build();
	}
}