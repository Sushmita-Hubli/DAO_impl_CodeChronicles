package com.example.demo.repositories;

import com.example.demo.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Transactional
    public void delete(User user) {
        User managedUser = entityManager.contains(user) ? user : entityManager.merge(user);
        entityManager.remove(managedUser);
    }

    @Transactional
    public User updateUser(User user) {
        return entityManager.merge(user);
    }

    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }
    
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        
        try {
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}