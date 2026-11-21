package com.example.library.service;

import com.example.library.dto.CreateReaderRequest;
import com.example.library.entity.Reader;
import com.example.library.exception.BusinessRuleException;
import com.example.library.exception.NotFoundException;
import com.example.library.repository.ReaderRepository;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReaderService {
    private final ReaderRepository readers;

    public ReaderService(ReaderRepository readers) { this.readers = readers; }

    @Transactional
    public Reader create(CreateReaderRequest req) {
        String email = req.email().trim().toLowerCase(Locale.ROOT);
        if (readers.existsByEmailIgnoreCase(email))
            throw new BusinessRuleException("Читач з таким email вже існує");
        return readers.save(new Reader(req.name(), email));
    }

    @Transactional(readOnly = true)
    public Reader getOrThrow(Long id) {
        return readers.findById(id).orElseThrow(() -> notFound(id));
    }

    /** Блокує рядок читача на час транзакції, щоб паралельні видачі не обійшли ліміт позик. */
    @Transactional
    public Reader getOrThrowForUpdate(Long id) {
        return readers.findByIdForUpdate(id).orElseThrow(() -> notFound(id));
    }

    private static NotFoundException notFound(Long id) {
        return new NotFoundException("Читача не знайдено: " + id);
    }
}
