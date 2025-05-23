DROP TABLE IF EXISTS copies CASCADE;
CREATE TABLE copies (
                        isbn text NOT NULL,
                        copy_id SERIAL PRIMARY KEY,
                        availability text NOT NULL,
                        FOREIGN KEY (isbn) references books(isbn)
);