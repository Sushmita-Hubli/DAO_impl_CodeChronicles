package com.example.demo.payloads;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	

	private int id;
	
	@NotEmpty(message="username must not be empty")
	@Size(min=4 , message="Username must be min of 4 characters")
	private String name;
	
	@NotEmpty(message="email cannot be empty")
	@Email(message="Email address is not valid!!")
	private String email;
	
	@NotEmpty(message="password must not be empty")
	@Size(min=3,message="password must be of minimum 3 characters")
	private String password;
	
	@NotEmpty(message="This field is mandatory")
	private String about;
	
	private Set<RoleDto> roles=new HashSet<>();
	
	
	@JsonIgnore
	public String getPassword()
	{
		return this.password;
	}
	
	@JsonProperty
	public void setPassword(String password)
	{
		this.password=password;
	}

}
