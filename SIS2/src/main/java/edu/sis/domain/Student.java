package edu.sis.domain;

import java.time.Instant;


public record Student(Long id, String firstName, String lastName, String email, Instant enrolledAt) {

    public Student {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName must not be blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName must not be blank");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("email is invalid");
        }
    }

    public Student withEnrolledAt(Instant value) {
        return new Student(id, firstName, lastName, email, value);
    }
}
