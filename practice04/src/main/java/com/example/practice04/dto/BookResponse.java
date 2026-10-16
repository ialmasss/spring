package com.example.practice04.dto;

import com.example.practice04.entity.Book;

import java.math.BigDecimal;

public record BookResponse(Long id, String title, String isbn, Integer year,
                           BigDecimal price, String description,
                           String authorName, String genreName) {

    public static BookResponse from(Book b) {
        return new BookResponse(b.getId(), b.getTitle(), b.getIsbn(), b.getYear(),
                b.getPrice(), b.getDescription(),
                b.getAuthor().getName(), b.getGenre().getName());
    }
}
