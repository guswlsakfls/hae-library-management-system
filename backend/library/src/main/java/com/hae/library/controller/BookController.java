package com.hae.library.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class BookController {
    @PostMapping(value = "/v1/book")
    public String createBook() {
        return "BookController";
    }

    @GetMapping(value = "/v1/book")
    public String getAllBook() {
        return "BookController";
    }

    @GetMapping(value = "/v1/book/{bookId}")
    public String getBookById() {
        return "BookController";
    }

    @PutMapping(value = "/v1/book/{bookId}")
    public String updateBookById() {
        return "BookController";
    }

    @DeleteMapping(value = "/v1/book/{bookId}")
    public String deleteBookById() {
        return "BookController";
    }
}
