package com.example.sisproject.mapper;

import com.example.sisproject.dto.BookRequest;
import com.example.sisproject.dto.BookResponse;
import com.example.sisproject.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest req) {
        Book book = new Book();
        book.setTitle(req.title());
        book.setAuthor(req.author());
        book.setPrice(req.price());
        return book;
    }

    public void updateEntity(Book book, BookRequest req) {
        book.setTitle(req.title());
        book.setAuthor(req.author());
        book.setPrice(req.price());
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice());
    }
}