package com.example.booklib.service;

import com.example.booklib.entity.Author;
import com.example.booklib.entity.Book;
import com.example.booklib.entity.Description;
import com.example.booklib.repository.AuthorRepository;
import com.example.booklib.repository.BookRepository;
import com.example.booklib.repository.DescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final DescriptionRepository descriptionRepository;
    private final AuthorRepository authorRepository;

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Transactional
    public void updateLibrary() {
        readAndSaveData("books.txt", this::parseBooks);
        readAndSaveData("bookDescription.txt", this::parseDescription);
        readAndSaveData("authors.txt", this::parseAuthors);
    }
    @Transactional
    public void readAndSaveData(String fileName, Consumer<String[]> dataProcessor) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            log.error("File {} not found", fileName);
            return;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            int lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                if (lineNumber <= 2) continue;
                if (!line.isEmpty()) {
                    String[] data = line.split("\\|");
                    dataProcessor.accept(data);
                }
            }
        } catch (IOException e) {
            log.error("Error reading file {}", fileName);
        }
    }

    @Transactional
    public void parseBooks(String[] data) {
        String nameBook = data[0].trim();
        String firstName = data[1].trim();
        String lastName = data[2].trim();
        String genre = data[3].trim();
        String shortDescription = data[4].trim();

        Author author = authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName,lastName)
                .orElseGet(() ->{
                    Author newAuthor = new Author();
                    newAuthor.setFirstName(firstName);
                    newAuthor.setLastName(lastName);
                    return authorRepository.save(newAuthor);
                });

        Book existBook = bookRepository.findByNameBookAndAuthor(nameBook, author);
        if (existBook != null) log.info("Book was found:-> {} ", existBook);
        else {
            Book book = new Book(nameBook, author, genre, shortDescription);
            bookRepository.save(book);
        }
    }
    @Transactional
    public void parseDescription(String[] data) {
        if (data.length < 2) {
            log.error("Invalid data format description");
            return;
        }
        String nameBook = data[0].trim();
        String fullDescription = data[1].trim();

        Book book = bookRepository.findByNameBook(nameBook)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        descriptionRepository.findByNameBook(nameBook).ifPresentOrElse(bookDescription -> {
                    bookDescription.setDescription(fullDescription);
                    bookDescription.setBook(book);
                    //log.error("Description {} was updated", bookDescription);
                    descriptionRepository.save(bookDescription);
                },
                () -> {
                    Description newDescription = new Description(nameBook, fullDescription);
                    newDescription.setNameBook(nameBook);
                    newDescription.setDescription(fullDescription);
                    newDescription.setBook(book);
                    descriptionRepository.save(newDescription);
                    log.info("Repository was updated");
                }
        );
    }
    public void parseAuthors(String[] data) {
        if (data.length < 4) {
            log.error("Invalid data format authors");
            return;
        }
        String firstName = data[0];
        String lastName = data[1];
        String photoPath = data[2];
        String biography = data[3];

        Optional<Author> findAuthor = authorRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);

        Author author;

        if (findAuthor.isPresent()) {
            author = findAuthor.get();
            author.setPhotoPath(photoPath);
            author.setBiography(biography);

            authorRepository.save(author);
            log.info("Author {} {} was found and update", firstName, lastName);
        } else {
            Author newAuthor = new Author();
            newAuthor.setFirstName(firstName);
            newAuthor.setLastName(lastName);
            newAuthor.setBiography(biography);
            newAuthor.setPhotoPath(photoPath);

            authorRepository.save(newAuthor);
            log.info("Author {} {} was created", firstName, lastName);
        }
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book with id " + id + " not found"));
    }

    public Description findDescriptionById(Long id) {
        return descriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Description with id " + id + " not found"));
    }

    public List<Book> findByNameBookOrAuthor(String query) {
        return bookRepository
        .findByNameBookContainingIgnoreCaseOrAuthor_FirstNameContainingIgnoreCaseOrAuthor_LastNameContainingIgnoreCase(query, query, query);
    }

    public List<Book> findByGenre(String genre) {
        return bookRepository.findByGenreOrderByNameBookAsc(genre);
    }
    public Author findAuthorById(Long id){
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author with id " + id + " not found"));
    }

}
