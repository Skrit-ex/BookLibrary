package com.example.booklib.repository;

import com.example.booklib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserNameAndEmail(String username, String email);

    Optional<User> findByEmail(String email);

    User findByUserName(String userName);
}
