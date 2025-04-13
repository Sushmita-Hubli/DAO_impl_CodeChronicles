package com.example.demo.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Comment;
import com.example.demo.entities.Post;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.payloads.CommentDto;
import com.example.demo.repositories.CommentRepo;
import com.example.demo.repositories.PostRepo;
import com.example.demo.repositories.UserRepo;
import com.example.demo.services.CommentService;

import jakarta.transaction.Transactional;

@Service
public class CommentServiceimpl implements CommentService {
	
	@Autowired
	PostRepo postrepo;
	
	@Autowired
	CommentRepo commentrepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	UserRepo userRepo;
	
	@Override
	@Transactional
	public CommentDto createComment(CommentDto commentdto, Integer postId, Integer userId) {
		// TODO Auto-generated method stub
		Post post = this.postrepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Comment com = this.modelMapper.map(commentdto, Comment.class);
		com.setId(null);
		com.setPost(post);
		com.setUser(user);
		Comment savedcomment = this.commentrepo.save(com);
		return this.modelMapper.map(savedcomment, CommentDto.class);
	}
	
	@Override
	@Transactional
	public void deleteComment(Integer commentId) {
		Comment com = this.commentrepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment Id", commentId));
		this.commentrepo.delete(com);
	}
	
	/**
	 * Delete comment with authorization checks
	 * 
	 * @param commentId The ID of the comment to delete
	 * @param postId The ID of the post containing the comment
	 * @param userId The ID of the user requesting deletion
	 * @throws ResourceNotFoundException If comment, post, or user not found
	 * @throws UnauthorizedException If user doesn't have permission to delete
	 */
	@Override
	@Transactional
	public void deleteCommentWithAuth(Integer commentId, Integer postId, Integer userId) {
		// 1. Fetch the comment
		Comment comment = this.commentrepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment Id", commentId));
		
		// 2. Fetch the post
		Post post = this.postrepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		
		// 3. Fetch the user
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		
		// 4. Check authorization
		boolean canDelete = false;
		
		// Case 1: User is the author of the comment
		if (comment.getUser().getId().equals(userId)) {
			canDelete = true;
		}
		// Case 2: User is the author of the post
		else if (post.getUser().getId().equals(userId)) {
			canDelete = true;
		}
		// Case 3: User is an admin - adjust role name based on your implementation
		else if (user.getRoles().stream().anyMatch(role -> 
				 role.getName().equals("ROLE_ADMIN") || 
				 role.getName().equals("ADMIN"))) {
			canDelete = true;
		}
		
		if (!canDelete) {
			throw new UnauthorizedException("You don't have permission to delete this comment");
		}
		
		// 5. If authorized, delete the comment
		this.commentrepo.delete(comment);
	}
}