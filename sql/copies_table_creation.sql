DROP TABLE IF EXISTS copies CASCADE;
CREATE TABLE copies (
    isbn text NOT NULL,
    copy_id SERIAL NOT NULL,
    availability text NOT NULL,
    UNIQUE (isbn, copy_id),
    FOREIGN KEY (isbn) references books(isbn)
);