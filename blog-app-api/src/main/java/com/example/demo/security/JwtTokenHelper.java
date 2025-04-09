package com.example.demo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component  //annotating a class with Component will help us Autowire it in other classes
public class JwtTokenHelper {
	
	public static final long JWT_TOKEN_VALIDITY=86400;
	
	
	private String secret="jwtTokenKey";
	
	//retrieving username from jwt token
	public String getUsernameFromToken(String token)
	{
		return getClaimFromToken(token,Claims::getSubject);
		
		
	}
	
	//retrieving expiration date from jwt token
	public Date getExpirationDateFromToken(String token)
	{
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public<T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver)
	{
		final Claims claims=getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//for retrieving any info from the token , we will need the secret key
	private Claims getAllClaimsFromToken(String token)
	{
		return Jwts.parser()
	            .setSigningKey(secret) // Use secret directly (old method)
	            .parseClaimsJws(token)
	            .getBody();
	}
	
	//checking if the token has expired 
	private Boolean isTokenExpired(String token)
	{
		final Date expiration=getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	//generate token for user
	public String generateToken(UserDetails userDetails)
	{
		Map<String, Object> claims=new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}
	
	//while creating the token-steps
	@SuppressWarnings("deprecation")
	private String doGenerateToken(Map<String,Object> claims, String subject)
	{
		return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();	
	}
	
	//validate token
	public Boolean validateToken(String token, UserDetails userDetails)
	{
		final String username=getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	

}
