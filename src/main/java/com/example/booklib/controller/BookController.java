package com.example.booklib.controller;


import com.example.booklib.dto.BookDto;
import com.example.booklib.dto.RegUserDto;
import com.example.booklib.entity.Book;
import com.example.booklib.service.BookService;
import com.example.booklib.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class BookController {

    @Autowired
     private BookService bookService;

    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public String getBook() {
        return "home";
    }

    @GetMapping("/getListOfBooks")
    public String processGetBook( Model model) {
        bookService.updateLibrary();
        List<Book> books = bookService.findAll();
        List<String> nameBooks = books.stream()
                .map(Book::getNameBook)
                .toList();
        model.addAttribute("nameBooks", nameBooks);
        return "getBooks";
    }

    @PostMapping("/getListOfBooks")
    public String processGetBook(@ModelAttribute("bookDto") BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Error with processGetBook: {}", bindingResult.getAllErrors());
        }
        return "getBooks";
    }

    @GetMapping("/regUser")
    public String regUser(Model model) {
        model.addAttribute("regUserDto", new RegUserDto());
        return "registration";
    }
    @RequestMapping("/regUser")
    public String saveUser(@ModelAttribute("regUserDto") RegUserDto regUserDto, BindingResult bindingResult) {
        userService.saveUser(regUserDto);
        if (bindingResult.hasErrors()){
            log.error("Error in saving user");
        }
        return "home";
    }
}
