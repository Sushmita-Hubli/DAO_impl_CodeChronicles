package com.example.demo.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Comment;
import com.example.demo.entities.Post;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.CommentDto;
import com.example.demo.repositories.CommentRepo;
import com.example.demo.repositories.PostRepo;
import com.example.demo.services.CommentService;

import jakarta.transaction.Transactional;

@Service
public class CommentServiceimpl implements CommentService{

	
	@Autowired
	PostRepo postrepo;
	
	@Autowired
	CommentRepo commentrepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	@Transactional
	public CommentDto createComment(CommentDto commentdto, Integer postId) {
		// TODO Auto-generated method stub
		Post post=this.postrepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post Id",postId));
		Comment com=this.modelMapper.map(commentdto, Comment.class);
//		post.setComments(com));  // so we will not do this bcoz one post can have many comments. and here we are creating one comment which we are setting to a post so we will do the following
		 com.setId(null);
		com.setPost(post);
		Comment savedcomment=this.commentrepo.save(com);
		return this.modelMapper.map(savedcomment, CommentDto.class);
	}

	@Override
	@Transactional
	public void deleteComment(Integer commentId) {
		// TODO Auto-generated method stub
		Comment com=this.commentrepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","Comment Id",commentId));
		this.commentrepo.delete(com);
		
	}

}
