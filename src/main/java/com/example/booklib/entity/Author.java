package com.example.booklib.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String photoPath;
    @Column(name = "biography", length =10020)
    private String biography;

    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private List<Book> books;


}
