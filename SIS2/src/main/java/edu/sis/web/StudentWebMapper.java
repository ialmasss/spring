package edu.sis.web;

import edu.sis.domain.Student;
import edu.sis.web.dto.StudentRequest;
import edu.sis.web.dto.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    Student toDomain(StudentRequest request);

    StudentResponse toResponse(Student student);
}
