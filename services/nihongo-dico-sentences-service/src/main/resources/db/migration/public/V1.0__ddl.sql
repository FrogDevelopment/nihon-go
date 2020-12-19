-- MISC
CREATE TABLE about
(
    about_id    SERIAL PRIMARY KEY,
    date_import TEXT NOT NULL,
    languages   json NOT NULL
);

CREATE EXTENSION IF NOT EXISTS pgroonga;
