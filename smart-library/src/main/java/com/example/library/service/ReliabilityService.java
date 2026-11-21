package com.example.library.service;

import com.example.library.dto.ReliabilityResponse;
import com.example.library.entity.Loan;
import com.example.library.repository.LoanRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReliabilityService {
    static final int PERIOD_DAYS = 30, MIN_RETURNED = 3, REGULAR_LIMIT = 2, RELIABLE_LIMIT = 5;

    private final LoanRepository loans;
    private final ReaderService readerService;
    private final Clock clock;

    public ReliabilityService(LoanRepository loans, ReaderService readerService, Clock clock) {
        this.loans = loans; this.readerService = readerService; this.clock = clock;
    }

    @Transactional(readOnly = true)
    public ReliabilityResponse evaluate(Long readerId) {
        readerService.getOrThrow(readerId);
        LocalDate today = LocalDate.now(clock);

        List<Loan> returned = loans
                .findByReaderIdAndReturnDateGreaterThanEqual(readerId, today.minusDays(PERIOD_DAYS))
                .stream().filter(l -> !l.isActive()).toList();
        boolean allOnTime = returned.stream().allMatch(Loan::wasReturnedOnTime);
        boolean hasOverdue = loans.findByReaderIdAndReturnDateIsNull(readerId)
                .stream().anyMatch(l -> l.isOverdueAt(today));

        boolean reliable = returned.size() >= MIN_RETURNED && allOnTime && !hasOverdue;
        return new ReliabilityResponse(readerId, reliable, reliable ? RELIABLE_LIMIT : REGULAR_LIMIT,
                returned.size(), allOnTime, hasOverdue);
    }
}
