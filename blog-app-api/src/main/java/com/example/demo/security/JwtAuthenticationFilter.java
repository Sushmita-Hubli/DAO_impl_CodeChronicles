package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 String requestURI = request.getRequestURI(); // Get request URL

		    // Bypass JWT authentication for public URLs
		 if (request.getMethod().equalsIgnoreCase("GET")) {
		        // Skip JWT filter for GET requests
		        filterChain.doFilter(request, response);
		        return;
		    }

		
        
		//1.GET TOKEN
		String requestToken=request.getHeader("Authorization");
		
		//our token will be like :  Bearer 898989shuhih
		System.out.println(requestToken);
		
		String username=null;
		String token=null;
		
		if(requestToken!=null && requestToken.startsWith("Bearer"))
		{
			token=requestToken.substring(7);
			try {
			username=this.jwtTokenHelper.getUsernameFromToken(token);
			}
			catch(Exception e)
			{
				System.out.println("Unable to get Jwt Token");
			}
			}
		else
		{
			System.out.println("Jwt token doesn't begin with Bearer");
		}
		
		//now we get the token, now we will validate it
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails userDetails=this.userDetailService.loadUserByUsername(username);
			if(this.jwtTokenHelper.validateToken(token, userDetails))
			{
				//token is valid 
				//now we will perform authentication
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			else
			{
				System.out.println("Invalid jwt Token");
			}
		}
		else
		{
			System.out.println("Username is null or context is not null");
		}
		
		filterChain.doFilter(request, response);
	}

}
