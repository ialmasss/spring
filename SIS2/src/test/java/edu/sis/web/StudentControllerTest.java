package edu.sis.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sis.domain.DuplicateEmailException;
import edu.sis.domain.Student;
import edu.sis.domain.StudentNotFoundException;
import edu.sis.service.StudentService;
import edu.sis.web.dto.StudentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(StudentWebMapperImpl.class)
class StudentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean StudentService service;

    private static final Student SAVED =
            new Student(1L, "Ann", "Lee", "ann@mail.com", Instant.parse("2026-09-01T10:00:00Z"));

    @Test
    void create_validRequest_returns201WithLocation() throws Exception {
        when(service.create(any(Student.class))).thenReturn(SAVED);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StudentRequest("Ann", "Lee", "ann@mail.com"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/students/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ann@mail.com"));
    }

    @Test
    void create_invalidEmail_returns400AndDoesNotCallService() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StudentRequest("Ann", "Lee", "not-an-email"))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void create_duplicateEmail_returns409() throws Exception {
        when(service.create(any(Student.class))).thenThrow(new DuplicateEmailException("ann@mail.com"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StudentRequest("Ann", "Lee", "ann@mail.com"))))
                .andExpect(status().isConflict());
    }

    @Test
    void get_existing_returnsStudent() throws Exception {
        when(service.getById(1L)).thenReturn(SAVED);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ann"));
    }

    @Test
    void get_missing_returns404() throws Exception {
        when(service.getById(99L)).thenThrow(new StudentNotFoundException(99L));

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Student with id 99 not found"));
    }

    @Test
    void list_returnsPage() throws Exception {
        when(service.findAll(any()))
                .thenReturn(new PageImpl<>(List.of(SAVED), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void delete_existing_returns204() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}
