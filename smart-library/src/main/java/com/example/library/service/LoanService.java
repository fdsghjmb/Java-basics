package com.example.library.service;

import com.example.library.dto.LoanRequest;
import com.example.library.entity.Book;
import com.example.library.entity.Loan;
import com.example.library.entity.Reader;
import com.example.library.exception.BusinessRuleException;
import com.example.library.exception.NotFoundException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {
    static final int LOAN_DAYS = 14;

    private final LoanRepository loans;
    private final BookRepository books;
    private final ReaderService readerService;
    private final ReliabilityService reliability;
    private final Clock clock;

    public LoanService(LoanRepository loans, BookRepository books, ReaderService readerService,
                       ReliabilityService reliability, Clock clock) {
        this.loans = loans; this.books = books; this.readerService = readerService;
        this.reliability = reliability; this.clock = clock;
    }

    @Transactional
    public Loan issue(LoanRequest req) {
        Reader reader = readerService.getOrThrowForUpdate(req.readerId());
        Book book = books.findByIdForUpdate(req.bookId())
                .orElseThrow(() -> new NotFoundException("Книгу не знайдено: " + req.bookId()));

        if (!book.isAvailable())
            throw new BusinessRuleException("Книга вже видана іншому читачу");

        int limit = reliability.evaluate(reader.getId()).maxActiveLoans();
        int active = loans.findByReaderIdAndReturnDateIsNull(reader.getId()).size();
        if (active >= limit)
            throw new BusinessRuleException("Перевищено ліміт активних позик: " + limit);

        book.markLoaned();
        return loans.save(new Loan(reader, book, LocalDate.now(clock), LOAN_DAYS));
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loans.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Позику не знайдено: " + loanId));
        if (!loan.isActive())
            throw new BusinessRuleException("Книгу вже повернуто");

        loan.close(LocalDate.now(clock));
        loan.getBook().markAvailable();
        return loan;
    }

    @Transactional(readOnly = true)
    public List<Loan> activeLoans(Long readerId) {
        readerService.getOrThrow(readerId);
        return loans.findByReaderIdAndReturnDateIsNull(readerId);
    }
}
