package com.example.trial.entity;

import java.io.Serializable;
import java.util.Objects;

public class BorrowedBookId implements Serializable {
    private String isbn;
    private String username;

    public BorrowedBookId() {}

    public BorrowedBookId(String isbn, String username) {
        this.isbn = isbn;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BorrowedBookId)) return false;
        BorrowedBookId that = (BorrowedBookId) o;
        return Objects.equals(isbn, that.isbn) &&
               Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, username);
    }
}
