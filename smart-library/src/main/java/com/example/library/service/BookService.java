package com.example.library.service;

import com.example.library.dto.CreateBookRequest;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final BookRepository books;

    public BookService(BookRepository books) { this.books = books; }

    @Transactional
    public Book create(CreateBookRequest req) { return books.save(new Book(req.title(), req.author())); }

    @Transactional(readOnly = true)
    public List<Book> findAll() { return books.findAll(); }
}
