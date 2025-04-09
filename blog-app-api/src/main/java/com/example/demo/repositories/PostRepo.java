package com.example.demo.repositories;

import com.example.demo.entities.Post;
import com.example.demo.entities.User;
import com.example.demo.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Post save(Post post) {
        if (post.getPostId() == null) {
            entityManager.persist(post);
        } else {
            post = entityManager.merge(post);
        }
        return post;
    }

    public Optional<Post> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Post.class, id));
    }

    public List<Post> findAll() {
        TypedQuery<Post> query = entityManager.createQuery("SELECT p FROM Post p", Post.class);
        return query.getResultList();
    }
    
    @Transactional
    public Post updatePost(Post post) {
        try {
          
            Post updatedPost = entityManager.merge(post);
            
            return updatedPost;
        } catch (Exception e) {
            
            throw e;
        }
    }

    @Transactional
    public void delete(Post post) {
        try {
            
            entityManager.remove(entityManager.contains(post) ? post : entityManager.merge(post));
           
        } catch (Exception e) {
           
            throw e;
        }
    }

    public Page<Post> findByCategory(Category category, Pageable pageable) {
        TypedQuery<Post> query = entityManager.createQuery(
            "SELECT p FROM Post p WHERE p.category = :category", Post.class);
        query.setParameter("category", category);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        TypedQuery<Long> countQuery = entityManager.createQuery(
            "SELECT COUNT(p) FROM Post p WHERE p.category = :category", Long.class);
        countQuery.setParameter("category", category);
        
        return new PageImpl<>(query.getResultList(), pageable, countQuery.getSingleResult());
    }

    public Page<Post> findByUser(User user, Pageable pageable) {
        TypedQuery<Post> query = entityManager.createQuery(
            "SELECT p FROM Post p WHERE p.user = :user", Post.class);
        query.setParameter("user", user);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        TypedQuery<Long> countQuery = entityManager.createQuery(
            "SELECT COUNT(p) FROM Post p WHERE p.user = :user", Long.class);
        countQuery.setParameter("user", user);
        
        return new PageImpl<>(query.getResultList(), pageable, countQuery.getSingleResult());
    }

    public List<Post> findByTitleContaining(String keyword) {
        TypedQuery<Post> query = entityManager.createQuery(
            "SELECT p FROM Post p WHERE p.title LIKE :keyword", Post.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }

    public Page<Post> findAll(Pageable pageable) {
        TypedQuery<Post> query = entityManager.createQuery("SELECT p FROM Post p", Post.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(p) FROM Post p", Long.class);
        
        return new PageImpl<>(query.getResultList(), pageable, countQuery.getSingleResult());
    }
}