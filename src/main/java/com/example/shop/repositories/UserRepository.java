package com.example.shop.repositories;

import com.example.shop.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
}
