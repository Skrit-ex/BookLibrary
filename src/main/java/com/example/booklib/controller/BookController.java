package com.example.booklib.controller;

import com.example.booklib.dto.BookDto;
import com.example.booklib.entity.Book;
import com.example.booklib.entity.Description;
import com.example.booklib.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/getListOfBooks")
    public String processGetBook(Model model) {
        bookService.updateLibrary();
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "getBooks";
    }

    @PostMapping("/getListOfBooks")
    public String processGetBook(@ModelAttribute("bookDto") BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Error with processGetBook: {}", bindingResult.getAllErrors());
        }
        return "getBooks";
    }

    @GetMapping("/fullBookDescription/{id}")
    public String getFullBookInfo(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        Description bookDescription = bookService.findDescriptionById(id);
        model.addAttribute("book", book);
        model.addAttribute("bookDescription", bookDescription);
        return "fullBookDescription";
    }

    @GetMapping("/getListOfBook")
    public String getListOfBook(@RequestParam String query, Model model) {
        List<Book> books = bookService.findByNameBookOrAuthor(query);
        model.addAttribute("books", books);
        return "findByNameBook";
    }
}



