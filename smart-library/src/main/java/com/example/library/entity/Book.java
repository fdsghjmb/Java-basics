package com.example.library.entity;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String author;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private BookStatus status = BookStatus.AVAILABLE;

    protected Book() {}
    public Book(String title, String author) { this.title = title; this.author = author; }

    public boolean isAvailable() { return status == BookStatus.AVAILABLE; }
    public void markLoaned() { status = BookStatus.LOANED; }
    public void markAvailable() { status = BookStatus.AVAILABLE; }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public BookStatus getStatus() { return status; }
}
