package com.example.demo.repositories;

import com.example.demo.entities.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class CommentRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Comment save(Comment comment) {
        
    	 try {
             if (comment.getId() == null || comment.getId() == 0) { // Explicitly check for new entities
                 entityManager.persist(comment);
             } else {
                 comment = entityManager.merge(comment);
             }
             return comment;
         } catch (Exception e) {
             throw e;
         }
           
       
    }


    public Optional<Comment> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Transactional
    public void delete(Comment comment) {
        try {
           
        	
            entityManager.remove(entityManager.contains(comment) ? comment : entityManager.merge(comment));
          
        } catch (Exception e) {
         
            throw e;
        }
    }
}