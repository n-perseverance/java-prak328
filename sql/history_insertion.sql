DELETE FROM history;
INSERT INTO history (isbn, copy_id, reader_id, issue_date, return_date)
    VALUES ((SELECT copies.isbn FROM copies WHERE copies.copy_id = 1), 1, 7, '01-01-2025', '31-01-2025'),
           ((SELECT copies.isbn FROM copies WHERE copies.copy_id = 2), 2, 8, '01-02-2025', '15-02-2025'),
           ((SELECT copies.isbn FROM copies WHERE copies.copy_id = 3), 3, 9, '01-01-2025', '01-02-2025'),
           ((SELECT copies.isbn FROM copies WHERE copies.copy_id = 4), 4, 10, '07-01-2025', '26-02-2025'),
           ((SELECT copies.isbn FROM copies WHERE copies.copy_id = 5), 5, 11, '05-02-2025', '01-03-2025');
