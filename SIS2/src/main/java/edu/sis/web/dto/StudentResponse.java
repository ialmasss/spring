package edu.sis.web.dto;

import java.time.Instant;

public record StudentResponse(Long id, String firstName, String lastName, String email, Instant enrolledAt) {
}
