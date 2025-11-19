package com.example.booklib.mapper;

import com.example.booklib.dto.BookDto;
import com.example.booklib.entity.Book;

public class BookMapper {

    public static Book bookDtotoBook(BookDto bookDto) {
        Book book = new Book();
        book.setNameBook(bookDto.getNameBook());
        book.setGenre(bookDto.getGenre());
        return book;
    }
}
