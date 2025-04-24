package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.demo.security.CustomUserDetailService;
import com.example.demo.security.JwtAuthenticationEntryPoint;
import com.example.demo.security.JwtAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;


@Configuration
@EnableWebSecurity
@EnableWebMvc
//@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http
		 .cors(cors -> cors.configurationSource(request -> {
	            CorsConfiguration config = new CorsConfiguration();
	            config.setAllowedOrigins(List.of("http://localhost:3000"));
	            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
	            config.setAllowedHeaders(List.of("*"));
	            config.setAllowCredentials(true);
	            return config;
	        }))
         .csrf(csrf -> csrf.disable()) // Disable CSRF for API authentication
         .authorizeHttpRequests(auth -> auth
        		 .requestMatchers(
        				    "/api/v1/auth/**", 
        				    "/api/v1/auth/login",
        				    "/api/v1/auth/register",
        				    "/api/categories/",
        				    "/api/posts",
        				    "/api/users/{userId}",
        				    "/v3/api-docs",
        				     "/api/category/{categoryId}/posts",
        				    "/api/posts/{postId}",
        	                "/v3/api-docs/**",              // Add this to allow all API docs endpoints
        	                "/v3/api-docs/swagger-config",  // Specifically add swagger config
        	                "/swagger-ui/**",               // Add Swagger UI
        	                "/swagger-ui.html" ,
        	                "/swagger-resources/**",
        	                "/webjars/**","/swagger-ui/index.html"// Add Swagger UI HTML
        	            ).permitAll()  .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
 // Public endpoints
             .anyRequest().authenticated() // All other endpoints require authentication
         )
          .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Custom error handling
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
          .authenticationProvider(authenticationProvider())
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT Filter

      return http.build();
	    } //This is called as Basic authentication
	 
	 
	 
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		 auth.userDetailsService(this.customUserDetailService).passwordEncoder(passwordEncoder());
	 }
	 
	 
	 @Bean
	 public PasswordEncoder passwordEncoder()
	 {
		 return new BCryptPasswordEncoder();
	 }
	 //now we will implement jwt authentication
	 

	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(customUserDetailService);
	        authProvider.setPasswordEncoder(passwordEncoder());
	        return authProvider;
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }
	    
//	    @Bean
//	    public FilterRegistrationBean coresFilter()
//	    {
//	    	UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
//	    	
//	    	CorsConfiguration corsConfiguration=new CorsConfiguration();
//	    	corsConfiguration.setAllowCredentials(true);
//	    	corsConfiguration.addAllowedOriginPattern("*");
//	    	corsConfiguration.addAllowedHeader("Authorization");
//	    	corsConfiguration.addAllowedHeader("Content-Type");
//	    	corsConfiguration.addAllowedHeader("Accept");
//	    	corsConfiguration.addAllowedMethod("POST");
//	    	corsConfiguration.addAllowedMethod("GET");
//	    	corsConfiguration.addAllowedMethod("DELETE");
//	    	corsConfiguration.addAllowedMethod("PUT");
//	    	corsConfiguration.addAllowedMethod("OPTIONS");
//	    	corsConfiguration.setMaxAge(3600L);
//	    	
//	    	
//	    	source.registerCorsConfiguration("/**", corsConfiguration);
//	    			
//	    	FilterRegistrationBean bean=new FilterRegistrationBean(new CorsFilter(source));
//	    	return bean;
//	    }
	    
	  
}
