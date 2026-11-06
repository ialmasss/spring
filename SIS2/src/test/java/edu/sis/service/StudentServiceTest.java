package edu.sis.service;

import edu.sis.domain.DuplicateEmailException;
import edu.sis.domain.Student;
import edu.sis.domain.StudentNotFoundException;
import edu.sis.persistence.StudentEntity;
import edu.sis.persistence.StudentEntityMapper;
import edu.sis.persistence.StudentEntityMapperImpl;
import edu.sis.persistence.StudentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private static final Instant NOW = Instant.parse("2026-09-01T10:00:00Z");

    @Mock
    StudentJpaRepository repository;

    StudentEntityMapper mapper = new StudentEntityMapperImpl();
    StudentService service;

    @BeforeEach
    void setUp() {
        service = new StudentService(repository, mapper, Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void create_setsEnrollmentDateAndReturnsSavedStudent() {
        when(repository.existsByEmailIgnoreCase("ann@mail.com")).thenReturn(false);
        when(repository.save(any(StudentEntity.class))).thenAnswer(inv -> {
            StudentEntity e = inv.getArgument(0);
            e.setId(42L);
            return e;
        });

        Student result = service.create(new Student(null, "Ann", "Lee", "ann@mail.com", null));

        ArgumentCaptor<StudentEntity> captor = ArgumentCaptor.forClass(StudentEntity.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getEnrolledAt()).isEqualTo(NOW);
        assertThat(result.id()).isEqualTo(42L);
        assertThat(result.enrolledAt()).isEqualTo(NOW);
    }

    @Test
    void create_withExistingEmail_throwsAndDoesNotSave() {
        when(repository.existsByEmailIgnoreCase("ann@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> service.create(new Student(null, "Ann", "Lee", "ann@mail.com", null)))
                .isInstanceOf(DuplicateEmailException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void getById_whenMissing_throwsNotFound() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(7L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("7");
    }

    @Test
    void update_changesFieldsButKeepsEnrollmentDate() {
        StudentEntity entity = entity(1L, "Ann", "Lee", "ann@mail.com");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Student result = service.update(1L, new Student(null, "Anna", "Smith", "ann@mail.com", null));

        assertThat(result.firstName()).isEqualTo("Anna");
        assertThat(result.lastName()).isEqualTo("Smith");
        assertThat(result.enrolledAt()).isEqualTo(NOW);
    }

    @Test
    void update_toEmailOfAnotherStudent_throwsDuplicate() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity(1L, "Ann", "Lee", "ann@mail.com")));
        when(repository.existsByEmailIgnoreCase("bob@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> service.update(1L, new Student(null, "Ann", "Lee", "bob@mail.com", null)))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(repository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(5L)).isInstanceOf(StudentNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void delete_existing_deletesById() {
        when(repository.existsById(5L)).thenReturn(true);

        service.delete(5L);

        verify(repository).deleteById(5L);
    }

    private StudentEntity entity(Long id, String first, String last, String email) {
        StudentEntity e = mapper.toEntity(new Student(id, first, last, email, NOW));
        return e;
    }
}
