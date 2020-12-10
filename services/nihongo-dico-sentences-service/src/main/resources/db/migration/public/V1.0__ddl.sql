-- IMPORT TABLES
CREATE TABLE i_sentences
(
    sentence_id INTEGER NOT NULL,
    lang        CHAR(4),
    sentence    TEXT
);

CREATE TABLE i_links
(
    sentence_id    INTEGER NOT NULL,
    translation_id INTEGER NOT NULL
);

CREATE TABLE i_japanese_indices
(
    sentence_id INTEGER NOT NULL,
    meaning_id  INTEGER NOT NULL,
    linking     TEXT
);

-- MISC
CREATE TABLE about
(
    about_id    SERIAL PRIMARY KEY,
    date_import TEXT NOT NULL,
    languages   json NOT NULL
);

CREATE EXTENSION IF NOT EXISTS pgroonga;
