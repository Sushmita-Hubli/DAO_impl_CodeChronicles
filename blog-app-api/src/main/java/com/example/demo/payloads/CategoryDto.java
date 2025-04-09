package com.example.demo.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor

@Getter
@Setter


public class CategoryDto {
private Integer categoryId;

@NotBlank
@Size(min=4, message="category Title must be of minimum 4 characters")
private String categoryTitle;

@NotBlank
@Size(min=3,message="category Description must be of minimum 3 characters")
private String categoryDescription;
}
