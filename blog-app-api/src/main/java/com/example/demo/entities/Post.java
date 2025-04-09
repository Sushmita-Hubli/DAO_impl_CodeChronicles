package com.example.demo.entities;

import jakarta.persistence.GenerationType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="post")
@NoArgsConstructor
@Getter
@Setter
public class Post {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer postId;
	
	@Column(name="post_title", length=100, nullable=false)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String imageName;
	
	private Date addedDate;
	
	@ManyToOne  //many posts can belong to an category
	@JoinColumn(name="categoryId") /*Without @JoinColumn, JPA would generate a default foreign key name like category_categoryId, which may not be ideal.
 With @JoinColumn(name="categoryId"), we explicitly define the column name.*/
	private Category category; //which category does the post belong to
	
	
	@ManyToOne  //many posts can be posted by an user
	private User user; //who have added the post
	
	
	@OneToMany(mappedBy="post",cascade=CascadeType.ALL) //bcoz one post can have many comments
	private Set<Comment> comments=new HashSet<>();
	
 

}
