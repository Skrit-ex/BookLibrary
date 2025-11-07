package com.example.booklib.entity;


import jakarta.persistence.*;
import lombok.*;

@Table (name = "fullDescription", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "book")
public class Description {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nameBook;

    @NonNull
    @Column(name = "description", length =1520)
    private String description;

    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public Description(String nameBook, @NonNull String description) {
        this.nameBook = nameBook;
        this.description = description;
    }
}
