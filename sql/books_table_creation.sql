DROP TABLE IF EXISTS books CASCADE;
CREATE TABLE books (
                       isbn text PRIMARY KEY,
                       name text NOT NULL,
                       authors text[] NOT NULL,
                       publisher text NOT NULL,
                       year date NOT NULL
);