package com.example.demo.repositories;

import com.example.demo.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Category saveCategory(Category category) {
        if (category.getCategoryId() == null) {
            entityManager.persist(category);
        } else {
            category = entityManager.merge(category);
        }
        return category;
    }

    @Transactional
    public Category save(Category category) {
        return saveCategory(category);
    }

    public Optional<Category> findById(Integer id) {
        return Optional.of(entityManager.find(Category.class, id));
    }

    public List<Category> findAll() {
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c", Category.class);
        return query.getResultList();
    }

    @Transactional
    public Category updateCategory(Category category) {
        return entityManager.merge(category);
    }

    @Transactional
    public void delete(Category category) {
        entityManager.remove(entityManager.contains(category) ? category : entityManager.merge(category));
    }
}
