INSERT INTO authors (name) VALUES
                               ('Лев Толстой'), ('Фёдор Достоевский'), ('Антон Чехов'),
                               ('Иван Тургенев'), ('Николай Гоголь');

INSERT INTO genres (name) VALUES
                              ('Роман'), ('Повесть'), ('Пьеса'), ('Рассказ');


INSERT INTO books (title, isbn, year, price, author_id, genre_id)
SELECT 'Книга №' || g,
       lpad(g::text, 13, '0'),
       1850 + (g % 70),
       round((5 + random() * 45)::numeric, 2),
       1 + (g % 5),
       1 + (g % 4)
FROM generate_series(1, 100) AS g;