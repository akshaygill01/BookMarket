package com.akshay.book.repository;

import com.akshay.book.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public Optional<User> getUserByEmail(String email);
}
