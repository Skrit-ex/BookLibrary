package com.example.booklib.service;

import com.example.booklib.entity.Book;
import com.example.booklib.entity.Description;
import com.example.booklib.repository.BookRepository;
import com.example.booklib.repository.DescriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;


    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Optional<Book> findByNameBook(String nameBook) {
        Optional<Book> optionalBook = bookRepository.findByNameBook(nameBook);
        if (optionalBook.isPresent()) {
            log.info("Book found: {}", optionalBook.get());
            return optionalBook;
        } else {
            log.error("Book not found: {}", nameBook);
            return Optional.empty();
        }
    }

    @Transactional
    public void updateLibrary(){
        readAndSaveData("books.txt", this::parseBooks);
        readAndSaveData("bookDescription.txt", this::parseDescription);
    }

    public void readAndSaveData(String fileName, Consumer<String[]> dataProcessor){
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if(inputStream == null){
            log.error("File {} not found", fileName);
            return;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            int lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null){
                lineNumber++;
                if(lineNumber <=2) continue;
                if(!line.isEmpty()){
                    String[] data = line.split("\\|");
                    dataProcessor.accept(data);
                }
        }
        } catch (IOException e) {
            log.error("Error reading file {}", fileName);
        }
    }

    public void parseBooks(String[] data){
        String nameBook = data[0].trim();
        String author = data[1].trim();
        String genre = data[2].trim();
        String shortDescription = data[3].trim();
        Book existBook = bookRepository.findByNameBookAndAuthor(nameBook,author);
        if(existBook != null) log.info("Book was found:-> {} ", existBook);
        else {
            Book book = new Book(nameBook,author,genre,shortDescription);
            bookRepository.save(book);
        }
    }
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
                    log.info("Description {} was updated", bookDescription);
                    descriptionRepository.save(bookDescription);
                },
                () -> {
                        Description newDescription = new Description(nameBook,fullDescription);
                        newDescription.setNameBook(nameBook);
                        newDescription.setDescription(fullDescription);
                        newDescription.setBook(book);
                        descriptionRepository.save(newDescription);
                        log.info("Repository was updated");
                    }
                );
        }
    }
