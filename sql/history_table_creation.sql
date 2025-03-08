DROP TABLE IF EXISTS history CASCADE;
CREATE TABLE history (
    isbn text NOT NULL,
    copy_id integer NOT NULL,
    reader_id integer NOT NULL,
    issue_date date NOT NULL,
    return_date date NOT NULL,
    FOREIGN KEY (isbn, copy_id) references copies(isbn, copy_id),
    FOREIGN KEY (reader_id) references readers(reader_id)
);