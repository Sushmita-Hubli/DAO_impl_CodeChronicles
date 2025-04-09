package com.example.demo.controllers;

import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.exceptions.ApiException;
import com.example.demo.exceptions.DuplicateEntryException;
import com.example.demo.payloads.JwtAuthRequest;
import com.example.demo.payloads.JwtAuthResponse;
import com.example.demo.payloads.UserDto;
import com.example.demo.security.JwtTokenHelper;
import com.example.demo.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/")

public class AuthController {
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	

	@PostMapping("/login")
	public ResponseEntity<?> createToken(@RequestBody JwtAuthRequest request) 
	{
		try {
		this.authenticate(request.getUsername(),request.getPassword());
		
		UserDetails userDetails=this.userDetailsService.loadUserByUsername(request.getUsername());
		
		String token=this.jwtTokenHelper.generateToken(userDetails);
		
		JwtAuthResponse response=new JwtAuthResponse();
		response.setToken(token);
		response.setUser(this.mapper.map((User) userDetails, UserDto.class));
		
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
		}
		catch (BadCredentialsException e) {
	        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
	    } catch (DisabledException e) {
	        return new ResponseEntity<>("User is disabled", HttpStatus.FORBIDDEN);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
	    }
		
	}


	private void authenticate(String username, String password) throws Exception {
	
		
		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
		
		try {
		this.authenticationManager.authenticate(authenticationToken);
		}
		catch (BadCredentialsException e) {
	        throw new ApiException("Invalid username or password");
	    } catch (DisabledException e) {
	        throw new DisabledException("User is disabled");
	    }
		
	}
	
	
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto)
	{
		try {
		UserDto registeredUser=this.userService.registerNewUser(userDto);
		return new ResponseEntity<UserDto>(registeredUser,HttpStatus.CREATED);
		}
		catch(DuplicateEntryException e)
		{
			return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
		}
	}
}
