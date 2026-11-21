package com.example.library.repository;

import com.example.library.entity.Loan;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReaderIdAndReturnDateIsNull(Long readerId);
    List<Loan> findByReaderIdAndReturnDateGreaterThanEqual(Long readerId, LocalDate from);
}
