package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.config.AppConstants;
import com.example.demo.entities.Role;
import com.example.demo.repositories.RoleRepo;

@SpringBootApplication
public class BlogAppApiApplication implements CommandLineRunner{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApiApplication.class, args);
	}
	
	@Bean  //this annotation will help in direct creation of object wherever we are going to Autowire this thing 
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}
	
	@Override
	public void run(String... args) throws Exception {
	    System.out.println(this.passwordEncoder.encode("xyz"));
	    
	    try {
	        Role role = new Role();
	        role.setId(AppConstants.Admin_User);
	        role.setName("ROLE_ADMIN");
	        
	        Role role1 = new Role();
	        role1.setId(AppConstants.Normal_User);
	        role1.setName("ROLE_NORMAL");
	        
	        // Use ArrayList instead of List.of() to create a mutable list
	        List<Role> roles = new ArrayList<>();
	        roles.add(role);
	        roles.add(role1);
	        
	        List<Role> results = this.roleRepo.saveAll(roles);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	 private static void printHibernateVersion() {
	        try {
	            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
	            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
	            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
	            String hibernateVersion = org.hibernate.Version.getVersionString();
	            System.out.println("Hibernate Version: " + hibernateVersion);
	            sessionFactory.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
