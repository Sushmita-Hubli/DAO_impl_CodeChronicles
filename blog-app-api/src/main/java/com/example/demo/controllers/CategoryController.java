package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.payloads.ApiResponse;
import com.example.demo.payloads.CategoryDto;
import com.example.demo.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	
	
	@Autowired
	CategoryService categoryService;
	

	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
	{
		CategoryDto cat=this.categoryService.createcategory(categoryDto);
		return new ResponseEntity<CategoryDto>(cat,HttpStatus.CREATED);
		
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updatecategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable Integer categoryId)
	{
		CategoryDto cat=this.categoryService.updateCategory(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(cat,HttpStatus.OK);
			
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse> deletecategory(@PathVariable Integer categoryId)
	{
		this.categoryService.deleteCategory(categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category Deleted successfully",true),HttpStatus.OK);
		
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getcategory(@PathVariable Integer categoryId)
	{
		
		CategoryDto cat=this.categoryService.getcategory(categoryId);
		return new ResponseEntity<CategoryDto>(cat,HttpStatus.OK);
	}
	
	
	
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getallcategories()
	{
		List<CategoryDto> categories=this.categoryService.getAllCategories();
		return new ResponseEntity<List<CategoryDto>>(categories,HttpStatus.OK);
		
	}

}
