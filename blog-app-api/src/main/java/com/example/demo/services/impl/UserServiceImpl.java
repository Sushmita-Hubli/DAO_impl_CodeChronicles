package com.example.demo.services.impl;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.AppConstants;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.payloads.UpdateUserDto;
import com.example.demo.payloads.UserDto;
import com.example.demo.repositories.RoleRepo;
import com.example.demo.repositories.UserRepo;
import com.example.demo.services.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.exceptions.*;

@Service
public class UserServiceImpl implements UserService {

	
	//very very important:   how are we autowiring userRepo since it is a interface and we cannot create object of s interface. we haven't even created a class which is going to implement this interface
	//answer: Spring Data JPA automatically provides an implementation of this interface at runtime. You don't need to write a class that implements UserRepo because Spring creates a proxy class for you behind the scenes.
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public UserDto createUser(@Valid UserDto userDto) {
		// TODO Auto-generated method stub
		User user=this.dtoToUser(userDto);  // dto to user 1st conversion
		User savedUser= this.userRepo.save(user); //saving the user to the database 2nd process
		return this.userToDto(savedUser);   //conversion of user to userDto again
		
	}

	@Override
	public UpdateUserDto updateUser(@Valid UpdateUserDto userDto, Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		
		User updatedUser=this.userRepo.save(user);
		return this.usertoUpdateUserDto(updatedUser);
		
		
		
	}

	@Override
	public UserDto getUserById(Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		// TODO Auto-generated method stub
		List<User> users=this.userRepo.findAll();
		List<UserDto> userDtos=users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		
		return userDtos;
	}

	@Override
	public String deleteUser(Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		// Check if the user has the ROLE_ADMIN role
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "Admins cannot delete other admins.";
        }

        // Remove all roles assigned to the user
        user.getRoles().clear(); // Clear the roles set
        this.userRepo.save(user); // Save the user without roles

        // Now delete the user
        this.userRepo.delete(user);
        return "User deleted successfully";

	}
	

	
	private User dtoToUser(UserDto userdto)
	{
//		User user=new User();
//		user.setId(userdto.getId());
//		user.setName(userdto.getName());
//		user.setEmail(userdto.getEmail());
//		user.setAbout(userdto.getAbout());
//		user.setPassword(userdto.getPassword());
//		return user;
		
		User user=this.modelMapper.map(userdto, User.class);
		return user;
		
	}
	
	private UserDto userToDto(User user)
	{
//		UserDto userdto=new UserDto();
//		userdto.setId(user.getId());
//		userdto.setAbout(user.getAbout());
//		userdto.setEmail(user.getEmail());
//		userdto.setName(user.getName());
//		userdto.setPassword(user.getPassword());
		
		UserDto userdto=this.modelMapper.map(user, UserDto.class);	
		return userdto;
		
	}
	
	private UpdateUserDto usertoUpdateUserDto(User user)
	{
		UpdateUserDto up=this.modelMapper.map(user, UpdateUserDto.class);
		return up;
	}

	@Override
	@Transactional
	public UserDto registerNewUser(UserDto userDto) {
		// TODO Auto-generated method stub
		
		
		if (userRepo.existsByEmail(userDto.getEmail())) {
	        throw new DuplicateEntryException("Email already exists!");
	        // OR use a custom exception:
	        // throw new DuplicateEmailException("Email already registered");
	    }
		User user=this.modelMapper.map(userDto, User.class);
		
		//changed here
		 user.setId(null); // Explicitly set ID to null for new user
		
		//Before saving this user to the db, we have to take care of the two things : 1. his password and 2. his roles
		
		//encoding the password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		//getting role of the user
		Role role=this.roleRepo.findById(AppConstants.Normal_User).get();
		
		user.getRoles().add(role);
		User newUser=this.userRepo.save(user);
		return this.modelMapper.map(newUser,UserDto.class);
		
	
		
	}

}
