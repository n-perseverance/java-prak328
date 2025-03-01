DROP TABLE IF EXISTS readers CASCADE;
CREATE TABLE readers (
    reader_id SERIAL PRIMARY KEY,
    name text NOT NULL,
    phone_number bigint NOT NULL,
    address text NOT NULL
);