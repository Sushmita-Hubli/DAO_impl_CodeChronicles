package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="categories")
@NoArgsConstructor
@Getter
@Setter

public class Category {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer categoryId;
	
	@Column(name="title",nullable=false)
	private String categoryTitle;
	
	@Column(name="description")
	private String categoryDescription;
	
	
	@OneToMany(mappedBy="category", cascade=CascadeType.ALL,fetch=FetchType.LAZY) 
	/*mappedBy = "category"
It defines the inverse side of the relationship.
The "category" here refers to the field name in the Post entity that holds the @ManyToOne reference.
It avoids creating an extra join table in the database.
cascade = CascadeType.ALL
This means that any operation (persist, merge, remove, refresh, detach) performed on a Category will be cascaded to its Post entities.
If a Category is deleted, all associated Post entities will also be deleted automatically.
fetch = FetchType.LAZY
By default, JPA will not fetch posts when loading a Category.
The posts will only be fetched when you explicitly access them (category.getPosts()).
This improves performance by avoiding unnecessary database queries.
❗ If using FetchType.EAGER instead:

Posts would be fetched immediately when retrieving a category.
This could lead to performance issues if there are many posts.*/
	private List<Post> posts=new ArrayList<>();   //so onecategory can have many posts

}
