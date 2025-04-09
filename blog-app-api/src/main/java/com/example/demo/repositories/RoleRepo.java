package com.example.demo.repositories;

import com.example.demo.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Role save(Role role) {
        try {
            entityManager.getTransaction().begin();
            if (role.getId() == null) {
                entityManager.persist(role);
            } else {
                role = entityManager.merge(role);
            }
            entityManager.getTransaction().commit();
            return role;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    @Transactional
    public Role updateRole(Role role) {
        try {
            entityManager.getTransaction().begin();
            Role updatedRole = entityManager.merge(role);
            entityManager.getTransaction().commit();
            return updatedRole;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }


    public Optional<Role> findById(Integer id) {
        return Optional.of(entityManager.find(Role.class, id));
    }

    public List<Role> findAll() {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }

    @Transactional
    public void delete(Role role) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(role) ? role : entityManager.merge(role));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    @Transactional
    public List<Role> saveAll(List<Role> roles) {
        List<Role> savedRoles = new ArrayList<>();
        for (Role role : roles) {
            if (role.getId() == null) {
                entityManager.persist(role);
            } else {
                role = entityManager.merge(role);
            }
            savedRoles.add(role);
            // Flush periodically to avoid performance issues
            if (savedRoles.size() % 20 == 0) {
                entityManager.flush();
            }
        }
        return savedRoles;
    }
}