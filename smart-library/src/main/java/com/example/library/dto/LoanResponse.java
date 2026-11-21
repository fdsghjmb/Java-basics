package com.example.library.dto;

import com.example.library.entity.Loan;
import java.time.LocalDate;

public record LoanResponse(Long id, Long readerId, Long bookId, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
    public static LoanResponse from(Loan l) {
        return new LoanResponse(l.getId(), l.getReader().getId(), l.getBook().getId(),
                l.getLoanDate(), l.getDueDate(), l.getReturnDate());
    }
}
