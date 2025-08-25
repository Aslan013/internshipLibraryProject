package com.example.trial.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrowed_books")
@IdClass(BorrowedBookId.class)
public class BorrowedBook {

    @Id
    @Column(name = "isbn")
    private String isbn;

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "bookname")
    private String bookName;

    @Column(name = "borrowing_date")
    private LocalDate borrowingDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Column(name = "last_return_date")
    private LocalDate lastReturnDate;

    // Getter ve Setter
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public LocalDate getBorrowingDate() { return borrowingDate; }
    public void setBorrowingDate(LocalDate borrowingDate) { this.borrowingDate = borrowingDate; }

    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public LocalDate getLastReturnDate() { return lastReturnDate; }
    public void setLastReturnDate(LocalDate lastReturnDate) { this.lastReturnDate = lastReturnDate; }
}
