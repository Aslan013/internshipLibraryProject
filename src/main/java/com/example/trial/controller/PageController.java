package com.example.trial.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.trial.repository.BookRepository;
import com.example.trial.repository.UserRepository;
import com.example.trial.repository.BorrowedBookRepository;

import jakarta.servlet.http.HttpSession;

import com.example.trial.entity.Book;
import com.example.trial.entity.BorrowedBook;
import com.example.trial.entity.BorrowedBookId;
import com.example.trial.entity.User;

import org.springframework.ui.Model;

@Controller
public class PageController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowedBookRepository borrowedBookRepository;

    // Constructor injection
    public PageController(UserRepository userRepository, BookRepository bookRepository, BorrowedBookRepository borrowedBookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.borrowedBookRepository = borrowedBookRepository;
    }



    @GetMapping("/main")
    public String mainPage() {
        return "main"; 
    }

    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/home")
    public String homePage(@RequestParam(value="username", required=false) String username, Model model) {
        model.addAttribute("username", username);

        // Status = true olan kitapları al ve random 5 tanesini seç
        List<Book> availableBooks = bookRepository.findByStatusTrue(); // repositoryde status = true filtreli
        Collections.shuffle(availableBooks);
        List<Book> suggestions = availableBooks.stream().limit(5).toList();

        model.addAttribute("suggestions", suggestions);

        // Veritabanından tüm kategorileri al ve modele ekle
        List<String> categories = bookRepository.findDistinctCategories(); // BookRepository'de bu metod olmalı
        model.addAttribute("categories", categories);

        return "home";
    }


    @GetMapping("/admin")
    public String adminPage(@RequestParam(value="username", required=false) String username, Model model) {
        model.addAttribute("username", username);
        return "admin"; // admin.html templates içinde
    }

    @GetMapping("/admin/borrowed-books")
    @ResponseBody
    public List<BorrowedBook> getAllBorrowedBooks() {
        LocalDate placeholder = LocalDate.of(2030, 1, 1);
        List<BorrowedBook> allBooks = borrowedBookRepository.findAll();
        List<BorrowedBook> currentBooks = new ArrayList<>();

        for (BorrowedBook book : allBooks) {
            if (book.getActualReturnDate().equals(placeholder)) {
                currentBooks.add(book);
            }
        }

        return currentBooks;
    }

    @PostMapping("/admin/add-user")
    @ResponseBody
    public Map<String, Object> addUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        if (userRepository.existsById(user.getUsername())) {
            result.put("success", false);
            result.put("message", "Kullanıcı adı zaten mevcut!");
            return result;
        }

        userRepository.save(user);
        result.put("success", true);
        result.put("message", "Kullanıcı başarıyla eklendi!");
        return result;
    }

    @GetMapping("/admin/user")
@ResponseBody
public User getUser(@RequestParam String username) {
    return userRepository.findById(username).orElse(null);
}
@GetMapping("/admin/users")
@ResponseBody
public List<User> getAllUsers() {
    return userRepository.findAll();
}
@DeleteMapping("/admin/delete-user")
@ResponseBody
public Map<String, Object> deleteUser(@RequestParam String username) {
    Map<String, Object> result = new HashMap<>();
    if (!userRepository.existsById(username)) {
        result.put("success", false);
        result.put("message", "Kullanıcı bulunamadı");
        return result;
    }
    userRepository.deleteById(username);
    result.put("success", true);
    result.put("message", "Kullanıcı başarıyla silindi");
    return result;
}
@GetMapping("/admin/books")
@ResponseBody
public List<Book> getAllBooks() {
    return bookRepository.findAll();
}

@DeleteMapping("/admin/delete-book")
@ResponseBody
public Map<String, Object> deleteBook(@RequestParam String isbn) {
    Map<String, Object> result = new HashMap<>();
    if(!bookRepository.existsById(isbn)) {
        result.put("success", false);
        result.put("message", "Kitap bulunamadı!");
        return result;
    }

    bookRepository.deleteById(isbn);
    result.put("success", true);
    result.put("message", "Kitap başarıyla silindi!");
    return result;
}




    
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        // Get current user from session
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/main"; // If no session, go back to login
        }
        
        // Get user details from database
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return "redirect:/main";
        }
        
        // Add user data to model
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    @ResponseBody
    public String updateProfile(@RequestParam String firstName,
                            @RequestParam String lastName,
                            @RequestParam String email,
                            @RequestParam String tc,
                            HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "error";
        }
        
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return "error";
        }
        
        // Update user fields
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setTc(tc);
        
        // Save to database
        userRepository.save(user);
        
        return "success";
    }

    @PostMapping("/profile/change-password")
    @ResponseBody
    public Map<String, String> changePassword(@RequestBody Map<String, String> payload,
                                            HttpSession session) {
        String username = (String) session.getAttribute("username"); // session’dan al
        String oldPass = payload.get("oldPassword");
        String newPass = payload.get("newPassword");

        Map<String, String> result = new HashMap<>();

        if (username == null) {
            result.put("message", "Oturum bulunamadı!");
            return result;
        }

        User user = userRepository.findByUsername(username);

        if (!user.getPassword().equals(oldPass)) {
            result.put("message", "Mevcut şifre yanlış!");
            return result;
        }

        user.setPassword(newPass);
        userRepository.save(user);

        result.put("message", "Şifre başarıyla değiştirildi!");
        return result;
    }


    @PostMapping("/profile/donate")
    @ResponseBody
    public Map<String, Object> donateBook(@RequestBody Map<String, String> payload,
                                        HttpSession session) {
        String username = (String) session.getAttribute("username");
        Map<String, Object> result = new HashMap<>();

        if(username == null) {
            result.put("success", false);
            result.put("message", "Oturum bulunamadı!");
            return result;
        }

        // Yeni kitap entity'si oluştur
        Book book = new Book();
        book.setTitle(payload.get("title"));
        book.setAuthorName(payload.get("authorName"));
        book.setAuthorSurname(payload.get("authorSurname"));
        book.setIsbn(payload.get("isbn"));
        book.setCategory(payload.get("category"));
        book.setPublisher(payload.get("publisher"));
        book.setPages(Integer.parseInt(payload.get("pages")));
        book.setPublishYear(Integer.parseInt(payload.get("publishYear")));
        book.setEdition(Integer.parseInt(payload.get("edition")));
        book.setStatus(true);

        bookRepository.save(book);

        result.put("success", true);
        result.put("message", "Kitap başarıyla bağışlandı!");
        return result;
    }


    @GetMapping("/profile/borrowed-books")
    @ResponseBody
    public Map<String, List<BorrowedBook>> getBorrowedBooks(HttpSession session) {
        String username = (String) session.getAttribute("username");
        List<BorrowedBook> allBooks = borrowedBookRepository.findByUsernameOrderByBorrowingDateDesc(username);

        LocalDate placeholder = LocalDate.of(2030, 1, 1);
        LocalDate today = LocalDate.now();

        List<BorrowedBook> currentBooks = new ArrayList<>();
        List<BorrowedBook> historyBooks = new ArrayList<>();

        for (BorrowedBook book : allBooks) {
            if (book.getActualReturnDate().equals(placeholder)) {
                currentBooks.add(book); // Henüz iade edilmemiş
            } else if (!book.getActualReturnDate().isAfter(today)) {
                historyBooks.add(book); // İade edilmiş ve bugün veya daha önce
            }
        }

        Map<String, List<BorrowedBook>> result = new HashMap<>();
        result.put("currentBooks", currentBooks);
        result.put("historyBooks", historyBooks);

        return result;
    }


    @PostMapping("/profile/return-book")
    @ResponseBody
    public Map<String, Object> returnBook(@RequestBody Map<String, String> payload) {
        String isbn = payload.get("isbn");
        String username = payload.get("username");

        BorrowedBook book = borrowedBookRepository.findById(new BorrowedBookId(isbn, username)).orElse(null);
        Map<String, Object> result = new HashMap<>();
        if(book == null) {
            result.put("success", false);
            return result;
        }

        book.setActualReturnDate(LocalDate.now());
        borrowedBookRepository.save(book);

        // Burada Book tablosundaki status alanını da true yapabilirsin
        Book b = bookRepository.findById(isbn).orElse(null);
        if(b != null) {
            b.setStatus(true); // status boolean alanı olmalı
            bookRepository.save(b);
        }

        result.put("success", true);
        return result;
    }


    @PostMapping("/books/borrow")
    @ResponseBody
    public Map<String, Object> borrowBook(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        String username = (String) session.getAttribute("username");
        if (username == null) {
            result.put("success", false);
            result.put("message", "Oturum bulunamadı!");
            return result;
        }

        String isbn = payload.get("isbn");
        Book book = bookRepository.findById(isbn).orElse(null);

        if (book == null || !book.getStatus()) {
            result.put("success", false);
            result.put("message", "Kitap mevcut değil veya zaten ödünç alınmış!");
            return result;
        }

        BorrowedBook borrowed = new BorrowedBook();
        borrowed.setIsbn(isbn);                        // ID alanı 1
        borrowed.setUsername(username);                // ID alanı 2
        borrowed.setBookName(book.getTitle());         // kitap adı ekleniyor
        borrowed.setBorrowingDate(LocalDate.now());    
        borrowed.setLastReturnDate(LocalDate.now().plusDays(30)); // planlanan dönüş
        borrowed.setActualReturnDate(LocalDate.of(2030, 1, 1));   // placeholder

        borrowedBookRepository.save(borrowed);

        // Ayrıca kitabın statüsünü false yap
        Book b = bookRepository.findById(isbn).orElse(null);
        if (b != null) {
            b.setStatus(false);
            bookRepository.save(b);
        }


        result.put("success", true);
        result.put("message", "Kitap başarıyla ödünç alındı!");
        return result;
    }





    @PostMapping("/main")
    public String mainUser(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,  // <- Add this line
                        Model model) {
        User user = userRepository.findById(username).orElse(null);

        if (user == null) {
            model.addAttribute("error", "Kullanıcı bulunamadı");
            return "main";
        }

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Şifre yanlış");
            return "main";
        }

        // Add these 3 lines:
        session.setAttribute("username", user.getUsername());
        session.setAttribute("userFullName", user.getFirstName() + " " + user.getLastName());
        session.setAttribute("isAdmin", user.getIsAdmin());

        model.addAttribute("username", user.getUsername());

        if (Boolean.TRUE.equals(user.getIsAdmin())) {
            return "redirect:/admin?username=" + user.getUsername();
        } else {
            return "redirect:/home?username=" + user.getUsername();
        }
    }
}
