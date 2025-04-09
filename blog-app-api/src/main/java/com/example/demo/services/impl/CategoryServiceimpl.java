package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Category;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.CategoryDto;
import com.example.demo.repositories.CategoryRepo;
import com.example.demo.services.CategoryService;

@Service
public class CategoryServiceimpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryDto createcategory(CategoryDto categoryDto) {
		// TODO Auto-generated method stub
		Category cat=this.modelMapper.map(categoryDto, Category.class);
		Category addedclass=this.categoryRepo.saveCategory(cat);
		return this.modelMapper.map(addedclass, CategoryDto.class);
			}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		// TODO Auto-generated method stub
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));
		cat.setCategoryTitle(categoryDto.getCategoryTitle());
		cat.setCategoryDescription(categoryDto.getCategoryDescription());
		Category updatedCategory=this.categoryRepo.save(cat);
		return this.modelMapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));
		this.categoryRepo.delete(cat);
			

	}

	@Override
	public CategoryDto getcategory(Integer categoryId) {
		// TODO Auto-generated method stub
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));
		return this.modelMapper.map(cat, CategoryDto.class);
		
		
		
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		// TODO Auto-generated method stub
		List<Category> categories=this.categoryRepo.findAll();
		List<CategoryDto> dtos=categories.stream().map(category->this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
		return dtos;
	}

	
}
