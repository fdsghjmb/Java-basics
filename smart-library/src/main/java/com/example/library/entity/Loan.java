package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private Reader reader;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private Book book;
    @Column(nullable = false) private LocalDate loanDate;
    @Column(nullable = false) private LocalDate dueDate;
    private LocalDate returnDate;

    protected Loan() {}
    public Loan(Reader reader, Book book, LocalDate loanDate, int days) {
        this.reader = reader; this.book = book;
        this.loanDate = loanDate; this.dueDate = loanDate.plusDays(days);
    }

    public boolean isActive() { return returnDate == null; }
    public boolean isOverdueAt(LocalDate today) { return isActive() && today.isAfter(dueDate); }
    public boolean wasReturnedOnTime() { return returnDate != null && !returnDate.isAfter(dueDate); }
    public void close(LocalDate date) { this.returnDate = date; }

    public Long getId() { return id; }
    public Reader getReader() { return reader; }
    public Book getBook() { return book; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
}
