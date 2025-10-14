package com.example.booklib.repository;

import com.example.booklib.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {

    Optional<Description> findByNameBook(String nameBook);
}
