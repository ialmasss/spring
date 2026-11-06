package edu.sis.service;

import edu.sis.domain.DuplicateEmailException;
import edu.sis.domain.Student;
import edu.sis.domain.StudentNotFoundException;
import edu.sis.persistence.StudentEntity;
import edu.sis.persistence.StudentEntityMapper;
import edu.sis.persistence.StudentJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentJpaRepository repository;
    private final StudentEntityMapper mapper;
    private final Clock clock;

    public StudentService(StudentJpaRepository repository, StudentEntityMapper mapper, Clock clock) {
        this.repository = repository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Transactional
    public Student create(Student student) {
        if (repository.existsByEmailIgnoreCase(student.email())) {
            throw new DuplicateEmailException(student.email());
        }
        StudentEntity saved = repository.save(mapper.toEntity(student.withEnrolledAt(clock.instant())));
        return mapper.toDomain(saved);
    }

    public Student getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Page<Student> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    public Page<Student> search(String text, Pageable pageable) {
        return repository.search(text, pageable).map(mapper::toDomain);
    }

    @Transactional
    public Student update(Long id, Student changes) {
        StudentEntity entity = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        if (!entity.getEmail().equalsIgnoreCase(changes.email())
                && repository.existsByEmailIgnoreCase(changes.email())) {
            throw new DuplicateEmailException(changes.email());
        }
        mapper.updateEntity(changes, entity);
        return mapper.toDomain(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
