CREATE TABLE authors (
                         id   BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE genres (
                        id   BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE books (
                       id         BIGSERIAL PRIMARY KEY,
                       title      VARCHAR(255)  NOT NULL,
                       isbn       VARCHAR(13)   NOT NULL UNIQUE,
                       year       INT,
                       price      NUMERIC(10,2) NOT NULL CHECK (price > 0),
                       author_id  BIGINT NOT NULL REFERENCES authors(id),
                       genre_id   BIGINT NOT NULL REFERENCES genres(id),
                       created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_books_author_id ON books(author_id);
CREATE INDEX idx_books_genre_id  ON books(genre_id);
CREATE INDEX idx_books_title     ON books(title);