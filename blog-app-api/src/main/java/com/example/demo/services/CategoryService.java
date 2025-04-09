package com.example.demo.services;

import java.util.List;

import com.example.demo.payloads.CategoryDto;

public interface CategoryService {
	
	CategoryDto createcategory(CategoryDto categoryDto); 
	CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);
	void deleteCategory(Integer categoryId);
	CategoryDto getcategory(Integer categoryId);
	List<CategoryDto> getAllCategories();

}
