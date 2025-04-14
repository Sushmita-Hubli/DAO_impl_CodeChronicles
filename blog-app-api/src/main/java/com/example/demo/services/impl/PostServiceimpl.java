package com.example.demo.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Post;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.PostDto;
import com.example.demo.payloads.PostResponse;
import com.example.demo.repositories.CategoryRepo;
import com.example.demo.repositories.PostRepo;
import com.example.demo.repositories.UserRepo;
import com.example.demo.services.PostService;
import com.example.demo.entities.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class PostServiceimpl implements PostService {
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","User Id",userId));
		Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));
		
		Post post=this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setCategory(category);
		post.setUser(user);
		Post createdPost=this.postRepo.save(post);
		return this.modelMapper.map(createdPost, PostDto.class);	
		}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		 Post post = this.postRepo.findById(postId)
		            .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));
		    
		    // Update fields only if they are provided in the DTO
		    if (postDto.getTitle() != null) {
		        post.setTitle(postDto.getTitle());
		    }
		    if (postDto.getContent() != null) {
		        post.setContent(postDto.getContent());
		    }
		    if (postDto.getImageName() != null) {
		        post.setImageName(postDto.getImageName());
		    }
		    
		    // Only update category if it's provided in the DTO
		    if (postDto.getCategory() != null && postDto.getCategory().getCategoryId() != null) {
		        Category category = this.categoryRepo.findById(postDto.getCategory().getCategoryId())
		                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", postDto.getCategory().getCategoryId()));
		        post.setCategory(category);
		    }
		    
		    Post updatedPost = this.postRepo.save(post);
		    return this.modelMapper.map(updatedPost, PostDto.class);
	}
	@Override
	public void deletePost(Integer postId) {
		// TODO Auto-generated method stub
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post id",postId));
		this.postRepo.delete(post);

	}

	@Override
	public PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy) {
		Pageable p=PageRequest.of(pageNumber, pageSize,Sort.by(Sort.Order.desc(sortBy)));
		Page<Post> pagePost=this.postRepo.findAll(p);
		List<Post> allPosts=pagePost.getContent();
		List<PostDto> postDtos= allPosts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse=new PostResponse();
		
		postResponse.setContent(postDtos);
		postResponse.setLastPage(pagePost.isLast());
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		
		return postResponse;
			}

	@Override
	public PostDto getPostById(Integer postId) {
		// TODO Auto-generated method stub
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post Id",postId));
		return this.modelMapper.map(post, PostDto.class);
	}
	
//getting post by category
	@Override
	public PostResponse getPostsByCategory(Integer categoryId,Integer pageNumber,Integer pageSize,String sortBy) {
		// TODO Auto-generated method stub
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));
		Pageable p=PageRequest.of(pageNumber, pageSize,Sort.by(sortBy));

		Page<Post> pagePost=this.postRepo.findByCategory(cat,p);
				
		// Extract posts from the Page object
	    List<Post> posts = pagePost.getContent();

	    // Convert Post entities to PostDto
	    List<PostDto> postDtos = posts.stream()
	            .map(post -> this.modelMapper.map(post, PostDto.class))
	            .collect(Collectors.toList());
	    
	    PostResponse postResponse = new PostResponse();
	    postResponse.setContent(postDtos);
	    postResponse.setLastPage(pagePost.isLast());
	    postResponse.setPageNumber(pagePost.getNumber());
	    postResponse.setPageSize(pagePost.getSize());
	    postResponse.setTotalElements(pagePost.getTotalElements());
	    postResponse.setTotalPages(pagePost.getTotalPages());

	    return postResponse;
	    
	}
	
	//getting posts by a user
	@Override
	public PostResponse getPostsByUser(Integer userId,Integer pageNumber,Integer pageSize,String sortBy) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","User Id",userId));
		Pageable page=PageRequest.of(pageNumber, pageSize,Sort.by(sortBy));
		Page<Post> post=this.postRepo.findByUser(user,page);
		List<Post> posts=post.getContent();
		 // Convert Post entities to PostDto
	    List<PostDto> postDtos = posts.stream()
	            .map(p -> this.modelMapper.map(p, PostDto.class))
	            .collect(Collectors.toList());
	    PostResponse postResponse = new PostResponse();
	    postResponse.setContent(postDtos);
	    postResponse.setLastPage(post.isLast());
	    postResponse.setPageNumber(post.getNumber());
	    postResponse.setPageSize(post.getSize());
	    postResponse.setTotalElements(post.getTotalElements());
	    postResponse.setTotalPages(post.getTotalPages());

	    return postResponse;
			
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts=this.postRepo.findByTitleContaining("%"+keyword+"%");
		return posts.stream().map(post->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		
	}
	
	
	

}
