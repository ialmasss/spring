CREATE TABLE students (
    id          BIGSERIAL    PRIMARY KEY,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    enrolled_at TIMESTAMPTZ  NOT NULL
);

CREATE UNIQUE INDEX uq_students_email_lower ON students (lower(email));
