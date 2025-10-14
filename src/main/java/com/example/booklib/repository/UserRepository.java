package com.example.booklib.repository;

import com.example.booklib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserNameAndEmail(String username, String email);
}
