package com.example.booklib.repository;

import com.example.booklib.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByNameBook(String nameBook);

    Book findByNameBookAndAuthor(String nameBook, String author);

    List<Book> findByNameBookContainingIgnoreCaseOrAuthorContainingIgnoreCase(String nameBook, String author);

    List<Book> findByGenreOrderByNameBookAsc(String genre);
}