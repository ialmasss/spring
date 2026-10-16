package com.example.practice04.repository;

import com.example.practice04.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"author", "genre"})
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "genre"})
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "genre"})
    Page<Book> findByAuthorId(Long authorId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "genre"})
    Page<Book> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    @Query(value = "select b from Book b join fetch b.author join fetch b.genre g where g.name = :genre",
            countQuery = "select count(b) from Book b where b.genre.name = :genre")
    Page<Book> findByGenreName(@Param("genre") String genre, Pageable pageable);

    Slice<Book> findByYearGreaterThan(Integer year, Pageable pageable);

    Page<BookSummary> findAllBy(Pageable pageable);

    boolean existsByIsbn(String isbn);

    @Modifying(clearAutomatically = true)
    @Query("update Book b set b.price = b.price * :k where b.genre.id = :genreId")
    int multiplyPriceByGenre(@Param("genreId") Long genreId, @Param("k") BigDecimal k);
}
