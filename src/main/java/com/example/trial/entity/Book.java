package com.example.trial.entity;

import jakarta.persistence.*;

@Entity
@Table(name="books")
public class Book {

    @Id
    @Column(name="isbn")
    private String isbn; // DB primary key

    @Column(name="bookname")
    private String title;

    @Column(name="authorname")
    private String authorName;

    @Column(name="authorsurname")
    private String authorSurname;

    @Column(name="publisher")
    private String publisher;

    @Column(name="publishyear")
    private Integer publishYear;

    @Column(name="edition")
    private Integer edition;

    @Column(name="status")
    private Boolean status;

    @Column(name="pages")
    private Integer pages;

    @Column(name="category")
    private String category;

    // getters & setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAuthorSurname() { return authorSurname; }
    public void setAuthorSurname(String authorSurname) { this.authorSurname = authorSurname; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }

    public Integer getEdition() { return edition; }
    public void setEdition(Integer edition) { this.edition = edition; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
