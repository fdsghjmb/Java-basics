package com.example.library.controller;

import com.example.library.dto.*;
import com.example.library.service.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LibraryController {
    private final ReaderService readers;
    private final BookService books;
    private final LoanService loans;
    private final ReliabilityService reliability;

    public LibraryController(ReaderService readers, BookService books, LoanService loans,
                             ReliabilityService reliability) {
        this.readers = readers; this.books = books; this.loans = loans; this.reliability = reliability;
    }

    @PostMapping("/readers") @ResponseStatus(HttpStatus.CREATED)
    public ReaderResponse createReader(@Valid @RequestBody CreateReaderRequest r) {
        return ReaderResponse.from(readers.create(r));
    }

    @PostMapping("/books") @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest r) {
        return BookResponse.from(books.create(r));
    }

    @GetMapping("/books")
    public List<BookResponse> allBooks() { return books.findAll().stream().map(BookResponse::from).toList(); }

    @PostMapping("/loans") @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse issue(@Valid @RequestBody LoanRequest r) { return LoanResponse.from(loans.issue(r)); }

    @PostMapping("/loans/{loanId}/return")
    public LoanResponse returnBook(@PathVariable Long loanId) { return LoanResponse.from(loans.returnBook(loanId)); }

    @GetMapping("/readers/{readerId}/reliability")
    public ReliabilityResponse reliability(@PathVariable Long readerId) { return reliability.evaluate(readerId); }

    @GetMapping("/readers/{readerId}/loans/active")
    public List<LoanResponse> active(@PathVariable Long readerId) {
        return loans.activeLoans(readerId).stream().map(LoanResponse::from).toList();
    }
}
