package com.example.trial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.trial.entity.BorrowedBook;
import com.example.trial.entity.BorrowedBookId;
import java.util.List;


public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, BorrowedBookId> {
    List<BorrowedBook> findByUsernameOrderByBorrowingDateDesc(String username);
}

