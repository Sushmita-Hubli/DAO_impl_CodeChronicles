package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.exceptions.ResourceNotFoundExceptionforStrings;
import com.example.demo.repositories.UserRepo;


@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//loading user from the database by username
		
		User user=this.userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundExceptionforStrings("User","email",username));
		return user;
		
	}

}
