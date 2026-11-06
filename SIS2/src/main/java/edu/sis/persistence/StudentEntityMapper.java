package edu.sis.persistence;

import edu.sis.domain.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface StudentEntityMapper {

    Student toDomain(StudentEntity entity);

    StudentEntity toEntity(Student student);

    default void updateEntity(Student source, @MappingTarget StudentEntity target) {
        target.setFirstName(source.firstName());
        target.setLastName(source.lastName());
        target.setEmail(source.email());
    }
}
