package com.example.practice04.service;

import com.example.practice04.dto.BookRequest;
import com.example.practice04.dto.BookResponse;
import com.example.practice04.dto.BookSummaryResponse;
import com.example.practice04.dto.PageResponse;
import com.example.practice04.entity.Book;
import com.example.practice04.repository.AuthorRepository;
import com.example.practice04.repository.BookRepository;
import com.example.practice04.repository.GenreRepository;
import com.example.practice04.web.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository books;
    private final AuthorRepository authors;
    private final GenreRepository genres;

    public BookService(BookRepository books, AuthorRepository authors, GenreRepository genres) {
        this.books = books;
        this.authors = authors;
        this.genres = genres;
    }

    public PageResponse<BookResponse> list(String title, Pageable pageable) {
        Page<Book> page = (title == null || title.isBlank())
                ? books.findAll(pageable)
                : books.findByTitleContainingIgnoreCase(title, pageable);
        return PageResponse.from(page.map(BookResponse::from));
    }

    public BookResponse get(Long id) {
        return BookResponse.from(find(id));
    }

    public PageResponse<BookSummaryResponse> summaries(Pageable pageable) {
        return PageResponse.from(books.findAllBy(pageable)
                .map(s -> new BookSummaryResponse(s.getId(), s.getTitle(), s.getPrice())));
    }

    @Transactional
    public BookResponse create(BookRequest r) {
        if (books.existsByIsbn(r.isbn())) {
            throw new IllegalStateException("ISBN already exists: " + r.isbn());
        }
        Book b = new Book();
        apply(b, r);
        return BookResponse.from(books.save(b));
    }

    @Transactional
    public BookResponse update(Long id, BookRequest r) {
        Book b = find(id);
        apply(b, r);
        return BookResponse.from(b);
    }

    @Transactional
    public void delete(Long id) {
        if (!books.existsById(id)) {
            throw new NotFoundException("Book " + id + " not found");
        }
        books.deleteById(id);
    }

    @Transactional
    public int changePriceByGenre(Long genreId, BigDecimal percent) {
        BigDecimal k = BigDecimal.ONE.add(percent.movePointLeft(2));
        return books.multiplyPriceByGenre(genreId, k);
    }

    private Book find(Long id) {
        return books.findById(id)
                .orElseThrow(() -> new NotFoundException("Book " + id + " not found"));
    }

    private void apply(Book b, BookRequest r) {
        b.setTitle(r.title());
        b.setIsbn(r.isbn());
        b.setYear(r.year());
        b.setPrice(r.price());
        b.setDescription(r.description());
        b.setAuthor(authors.findById(r.authorId())
                .orElseThrow(() -> new NotFoundException("Author " + r.authorId() + " not found")));
        b.setGenre(genres.findById(r.genreId())
                .orElseThrow(() -> new NotFoundException("Genre " + r.genreId() + " not found")));
    }
}
