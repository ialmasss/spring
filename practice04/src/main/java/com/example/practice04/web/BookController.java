package com.example.practice04.web;

import com.example.practice04.dto.BookRequest;
import com.example.practice04.dto.BookResponse;
import com.example.practice04.dto.BookSummaryResponse;
import com.example.practice04.dto.PageResponse;
import com.example.practice04.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<BookResponse> list(
            @RequestParam(required = false) String title,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return service.list(title, pageable);
    }

    @GetMapping("/summaries")
    public PageResponse<BookSummaryResponse> summaries(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return service.summaries(pageable);
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest req) {
        BookResponse created = service.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping("/prices")
    public Map<String, Integer> changePrices(@RequestParam Long genreId,
                                             @RequestParam @Positive BigDecimal percent) {
        return Map.of("updated", service.changePriceByGenre(genreId, percent));
    }
}
