package com.example.booklib.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table (name = "englishBook",uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_book")
    private String nameBook;
    private String author;
    private String genre;
    private String description;

    public Book(String nameBook, String author, String genre, String description) {
        this.nameBook = nameBook;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }
}
