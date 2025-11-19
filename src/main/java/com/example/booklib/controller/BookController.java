package com.example.booklib.controller;

import com.example.booklib.dto.BookDto;
import com.example.booklib.entity.Author;
import com.example.booklib.entity.Book;
import com.example.booklib.entity.Description;
import com.example.booklib.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String processGetBook(@RequestParam(defaultValue = "0") int page, //NOTE: номер страницы (по умолчанию 0 → первая страница)
                                 @RequestParam(defaultValue = "10") int size, //NOTE: сколько книг показывать на странице (по умолчанию 10).
                                 @RequestParam(defaultValue = "nameBook") String sort, //NOTE: по какому полю сортировать (по умолчанию nameBook)
                                 @RequestParam(defaultValue = "asc") String direction, //NOTE: направление сортировки (asc или desc)
                                 Model model) {

        Sort sortOrder = direction.equalsIgnoreCase("asc")
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);                  //NOTE: PageRequest объединяет номер страницы, размер страницы и сортировку.

        Page<Book> bookPage = bookService.findAll(pageable);

        model.addAttribute("currentPage", bookPage.getNumber());        //NOTE:  Внутри Page<Book> хранится:
        model.addAttribute("totalPages", bookPage.getTotalPages());     // список книг для текущей страницы (getContent()),
        model.addAttribute("books", bookPage.getContent());             // общее количество страниц (getTotalPages()),
        model.addAttribute("totalItems", bookPage.getTotalElements());  // общее количество элементов (getTotalElements()).
        model.addAttribute("sortFields", sort);                         // номер страницы (getNumber()),
        model.addAttribute("sortDir", direction);
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

    @GetMapping("/searchBooks")
    public String getListOfBook(@RequestParam String query, Model model) {
        List<Book> books = bookService.findByNameBookOrAuthor(query);
        model.addAttribute("books", books);
        return "findByNameBook";
    }

    @GetMapping("/sortGenre")
    public String sortGenre(@RequestParam String genre, Model model) {
        List<Book> books = bookService.findByGenre(genre);
        model.addAttribute("books", books);
        model.addAttribute("genre", genre);
        return "getBooks";
    }

    @GetMapping("/updateLibrary")
    public String updateLibrary(Model model) {
        try {
            bookService.updateLibrary();
            return "redirect:/book/getListOfBooks";
        } catch (Exception e) {
            log.error("Error with updateLibrary ", e);
            model.addAttribute("error", "Error with updateLibrary");
            return "errorUpdateLibrary";
        }
    }
    @GetMapping("/getAuthor/{id}")
    public String getAuthor(@PathVariable Long id, Model model) {
        Author findAuthor = bookService.findAuthorById(id);
        model.addAttribute("author", findAuthor);
        return "author";
    }
}



