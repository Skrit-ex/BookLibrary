package com.example.booklib.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table (name = "englishBook",uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "description")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_book")
    private String nameBook;
    private String author;
    private String genre;
    private String shortDescription;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Description description;

    public Book(String nameBook, String author, String genre, String shortDescription) {
        this.nameBook = nameBook;
        this.author = author;
        this.genre = genre;
        this.shortDescription = shortDescription;
    }
}
