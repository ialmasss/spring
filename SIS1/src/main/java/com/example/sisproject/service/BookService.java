package com.example.sisproject.service;

import com.example.sisproject.dto.BookRequest;
import com.example.sisproject.dto.BookResponse;
import com.example.sisproject.entity.Book;
import com.example.sisproject.exception.NotFoundException;
import com.example.sisproject.mapper.BookMapper;
import com.example.sisproject.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        return mapper.toResponse(findOrThrow(id));
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        Book saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = findOrThrow(id);
        mapper.updateEntity(book, request);
        return mapper.toResponse(repository.save(book));
    }

    @Transactional
    public void delete(Long id) {
        Book book = findOrThrow(id);
        repository.delete(book);
    }

    private Book findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
    }
}