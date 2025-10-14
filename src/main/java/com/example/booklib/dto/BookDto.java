package com.example.booklib.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    @Pattern(regexp = "([A-Za-z])*", message = "The BookName isn't correct, try again")
    private String nameBook;

    @Pattern(regexp = "([A-Za-z])*", message = "The Author isn't correct, try again")
    private String author;

    @Pattern(regexp = "([A-Za-z])*", message = "The genre of book isn't correct, try again")
    private String genre;

    @Pattern(regexp = "([А-ЯA-Z][^.!?]*)" , message = "Check your description")
    private String description;
}
