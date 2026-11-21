package com.example.library.dto;

import com.example.library.entity.Book;
import com.example.library.entity.BookStatus;

public record BookResponse(Long id, String title, String author, BookStatus status) {
    public static BookResponse from(Book b) { return new BookResponse(b.getId(), b.getTitle(), b.getAuthor(), b.getStatus()); }
}
