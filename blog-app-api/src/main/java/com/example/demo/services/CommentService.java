package com.example.demo.services;

import com.example.demo.payloads.CommentDto;

public interface CommentService {
	
	CommentDto createComment(CommentDto commentdto,Integer postId,Integer userId);
	

	void deleteComment(Integer commentId);
	
	 void deleteCommentWithAuth(Integer commentId, Integer postId, Integer userId);
}
