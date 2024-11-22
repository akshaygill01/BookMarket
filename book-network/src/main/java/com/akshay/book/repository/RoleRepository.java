package com.akshay.book.repository;

import com.akshay.book.bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface RoleRepository extends JpaRepository<Role, Integer> {
        Optional<Role> findByName(String name);
    }
