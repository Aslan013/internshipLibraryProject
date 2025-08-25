package com.example.trial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import com.example.trial.entity.Book;

public interface BookRepository extends JpaRepository<Book, String> {
    
    // Search by title or author (case insensitive)
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT(:searchTerm, '%')) " +
       "OR LOWER(b.authorName) LIKE LOWER(CONCAT(:searchTerm, '%')) " +
       "OR LOWER(b.authorSurname) LIKE LOWER(CONCAT(:searchTerm, '%'))" + "OR LOWER(b.category) LIKE LOWER(CONCAT(:searchTerm, '%'))")
    List<Book> searchBooks(@Param("searchTerm") String searchTerm);

    List<Book> findByStatusTrue();

    @Query("SELECT DISTINCT b.category FROM Book b")
    List<String> findDistinctCategories();


}