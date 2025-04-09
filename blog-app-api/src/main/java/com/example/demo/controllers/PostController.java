package com.example.demo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.AppConstants;
import com.example.demo.payloads.ApiResponse;
import com.example.demo.payloads.PostDto;
import com.example.demo.payloads.PostResponse;
import com.example.demo.services.FileService;
import com.example.demo.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.*;

@RestController
@RequestMapping("/api/")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileservice;
	
	@Value("${project.image}")
	private String path;
	

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer categoryId)
	
	{
		PostDto post=this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(post,HttpStatus.CREATED);
	}

	//get posts by a particular user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getpostsbyuser(@PathVariable Integer userId,@RequestParam(value="pageNumber",defaultValue="0",required=false)Integer pageNumber,@RequestParam(value="pageSize",defaultValue="5",required=false)Integer pageSize,
			@RequestParam(value="sortBy",defaultValue="postId",required=false)String sortBy
			)
	{
		PostResponse posts=this.postService.getPostsByUser(userId,pageNumber,pageSize,sortBy);
		return new ResponseEntity<PostResponse>(posts,HttpStatus.OK);
	}
	
	
	//get posts by category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostResponse> getpostsByCategory(@PathVariable Integer categoryId,@RequestParam(value="pageNumber",defaultValue="0",required=false)Integer pageNumber, @RequestParam(value="pageSize",defaultValue="5",required=false)Integer pageSize,@RequestParam(value="sortBy",defaultValue="postId",required=false)String sortBy
			)
	{
		PostResponse posts=this.postService.getPostsByCategory(categoryId,pageNumber,pageSize,sortBy);
		return new ResponseEntity<PostResponse>(posts,HttpStatus.OK);
	}
	
	
	//get all posts
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getallposts(
			@RequestParam(value="pageNumber",defaultValue=AppConstants.page_Number,required=false) Integer pageNumber,
			@RequestParam(value="pageSize",defaultValue=AppConstants.page_Size,required=false)Integer pageSize,
			@RequestParam(value="sortBy",defaultValue=AppConstants.sort_By,required=false)String sortBy
			)
	{
		PostResponse postResponse=this.postService.getAllPost(pageNumber,pageSize,sortBy);
		return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
	}
	
	
	//get post by post id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getpostbyid(@PathVariable Integer postId)
	{
		PostDto post=this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(post,HttpStatus.OK);
	}
	
	
	
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletepost(@PathVariable Integer postId)
	{
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully",true),HttpStatus.OK);
	}
	
	//UPDATE A POST
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatepost(@RequestBody PostDto postDto, @PathVariable Integer postId)
	{
		PostDto post=this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(post,HttpStatus.OK);
		
	}
	
	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> getpostsbykeyword(@PathVariable String keyword)
	{
		List<PostDto> posts=this.postService.searchPosts(keyword);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	
	
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,@PathVariable Integer postId) throws IOException
	{
		PostDto postdto=this.postService.getPostById(postId);
		
		String fileName= this.fileservice.uploadImage(path, image);
		postdto.setImageName(fileName);
		return new ResponseEntity<PostDto>(this.postService.updatePost(postdto, postId),HttpStatus.OK);
		
	}
	
	
	@GetMapping(value="/post/image/{imageName}",produces=MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException
	{
		InputStream resource=this.fileservice.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
}
