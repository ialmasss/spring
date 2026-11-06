package edu.sis.web;

import edu.sis.service.StudentService;
import edu.sis.web.dto.PageResponse;
import edu.sis.web.dto.StudentRequest;
import edu.sis.web.dto.StudentResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;
    private final StudentWebMapper mapper;

    public StudentController(StudentService service, StudentWebMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        StudentResponse body = mapper.toResponse(service.create(mapper.toDomain(request)));
        return ResponseEntity.created(URI.create("/api/students/" + body.id())).body(body);
    }

    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @GetMapping
    public PageResponse<StudentResponse> list(@RequestParam(required = false) String q,
                                              @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
        var page = (q == null || q.isBlank()) ? service.findAll(pageable) : service.search(q, pageable);
        return PageResponse.of(page.map(mapper::toResponse));
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return mapper.toResponse(service.update(id, mapper.toDomain(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
