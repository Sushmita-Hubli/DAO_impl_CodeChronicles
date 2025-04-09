package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Comment;
import com.example.demo.payloads.ApiResponse;
import com.example.demo.payloads.CommentDto;
import com.example.demo.services.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@PostMapping("/post/{postId}")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentdto,@PathVariable Integer postId)
	{
	CommentDto comm=this.commentService.createComment(commentdto, postId);
	return new ResponseEntity<CommentDto>(comm,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/post/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId)
	{
		this.commentService.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment Deleted Successfully",true),HttpStatus.OK);
	}

}
