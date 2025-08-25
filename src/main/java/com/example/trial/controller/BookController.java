package com.example.trial.controller;

import com.example.trial.entity.Book;
import com.example.trial.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Book> books;
        if (query == null || query.trim().isEmpty()) {
            books = repo.findAll();
        } else {
            String searchTerm = query.trim();
            books = repo.searchBooks(searchTerm);
        }

        model.addAttribute("books", books);
        model.addAttribute("query", query);
        return "search"; // templates/search.html
    }
}
