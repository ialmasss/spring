package edu.sis.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Runs against a real PostgreSQL (Testcontainers); the schema is created by the same Flyway
 * migrations as in production. Requires Docker.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class StudentJpaRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired StudentJpaRepository repository;

    @Test
    void findByEmailIgnoreCase_findsRegardlessOfCase() {
        repository 
    }

    @Test
    void uniqueIndexOnLowerEmail_rejectsDuplicates() {
        repository.saveAndFlush(student("Ann", "Lee", "ann@mail.com"));

        assertThatThrownBy(() -> repository.saveAndFlush(student("Bob", "Ray", "ANN@mail.com")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void search_matchesFirstOrLastName_withPagingAndSorting() {
        repository.save(student("Ann", "Lee", "ann@mail.com"));
        repository.save(student("Leon", "Black", "leon@mail.com"));
        repository.save(student("Bob", "Ray", "bob@mail.com"));

        var page = repository.search("le", PageRequest.of(0, 10, Sort.by("lastName")));

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).extracting(StudentEntity::getLastName)
                .containsExactly("Black", "Lee");
    }

    private StudentEntity student(String first, String last, String email) {
        StudentEntity e = new StudentEntityMapperImpl().toEntity(
                new edu.sis.domain.Student(null, first, last, email, Instant.now()));
        return e;
    }
}
