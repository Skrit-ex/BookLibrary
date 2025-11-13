package com.example.booklib.controller;


import com.example.booklib.dto.BookDto;
import com.example.booklib.dto.LoginDto;
import com.example.booklib.dto.RegUserDto;
import com.example.booklib.entity.Book;
import com.example.booklib.entity.Description;
import com.example.booklib.service.BookService;
import com.example.booklib.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final UserService userService;


    @GetMapping("/home")
    public String home(Model model) {
//NOTE:        User user = userService.getCurrentUser();
//        model.addAttribute("username", user.getUserName());
              Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
              if(authentication instanceof AnonymousAuthenticationToken) {
                  model.addAttribute("username", "Guest");
              }else {
                  model.addAttribute("username", authentication.getName());
              }
        return "home";
    }

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

    @GetMapping("/regUser")
    public String regUser(Model model) {
        model.addAttribute("regUserDto", new RegUserDto());
        return "registration";
    }

    @PostMapping("/regUser")
    public String saveUser(@ModelAttribute("regUserDto") RegUserDto regUserDto, BindingResult bindingResult) {
        userService.saveUser(regUserDto);
        if (bindingResult.hasErrors()) {
            log.error("Error in saving user");
        }
        return "home";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }
    @GetMapping("/loginError")
    public String loginError() {
        return "loginError";
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



