package edu.sis.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long> {


    Optional<StudentEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);


    @Query("""
           select s from StudentEntity s
           where lower(s.lastName) like lower(concat('%', :text, '%'))
              or lower(s.firstName) like lower(concat('%', :text, '%'))
           """)
    Page<StudentEntity> search(@Param("text") String text, Pageable pageable);
}
