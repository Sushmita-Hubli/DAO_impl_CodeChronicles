package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.UserService;

import jakarta.validation.Valid;

import com.example.demo.payloads.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//POST-create user
	
	//This code listens for a GET request at the root URL ("/"), takes user data from the request, creates a new user using a service, and returns the created user's details with a 201 Created status.
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody  UserDto userDto)  //The method takes a UserDto object as input, which is automatically created from the JSON data sent in the request body (thanks to @RequestBody).This userDto contains the details of the user to be created.
	{
		//The @RequestBody annotation in Spring Boot is used to extract data from the HTTP request body and automatically convert it into a Java object.
		UserDto createUserDto=this.userService.createUser(userDto);
		return new ResponseEntity<UserDto>(createUserDto, HttpStatus.CREATED); //Use this when you want to return a specific status code that is not 200 OK. For example:

//201 Created for successful resource creation.

//204 No Content for successful deletion.
//
//400 Bad Request for invalid input.
		
		//so this method is going to send us a response with the user details which we will enter in postman along with the status code of 201 created
	}
	
	
	@PutMapping("/{userId}")
	public ResponseEntity<UpdateUserDto> updateUser(@Valid @RequestBody UpdateUserDto userDto, @PathVariable Integer userId)
	{
		UpdateUserDto updatedUser=this.userService.updateUser(userDto, userId);
		return ResponseEntity.ok(updatedUser);  //This is a shortcut method provided by ResponseEntity to create a response with an HTTP status code of 200 OK.
	}
	
	
	
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId)
	{
		String s=this.userService.deleteUser(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(s,true), HttpStatus.OK);
		
	}
	
	
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers()
	{
		return ResponseEntity.ok(this.userService.getAllUsers());
	}
	
	
	//get user by id
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getsingleUser(@PathVariable Integer userId)
	{
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}
	
	
	//search user by keyword
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> getuserbykeyword(@PathVariable String keyword)
	{
		List<UserDto> users=this.userService.searchUsers(keyword);
		return new ResponseEntity<List<UserDto>>(users,HttpStatus.OK);
	}
	

}
