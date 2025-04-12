package com.example.demo.services;

import java.util.List;

import com.example.demo.payloads.UpdateUserDto;
import com.example.demo.payloads.UserDto;

public interface UserService {
	
	UserDto registerNewUser(UserDto user);
	
	UserDto createUser(UserDto user);
	
	UpdateUserDto updateUser(UpdateUserDto user, Integer userId);
	
	UserDto getUserById(Integer userId);
	
	List<UserDto> getAllUsers();
	
	String deleteUser(Integer userId);
	
	
	
	
	
	

}
