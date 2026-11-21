package com.example.library.dto;

import com.example.library.entity.Reader;

public record ReaderResponse(Long id, String name, String email) {
    public static ReaderResponse from(Reader r) { return new ReaderResponse(r.getId(), r.getName(), r.getEmail()); }
}
