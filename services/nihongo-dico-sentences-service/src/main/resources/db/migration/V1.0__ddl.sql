CREATE TABLE about
(
    about_id    SERIAL PRIMARY KEY,
    date_import TEXT NOT NULL,
    languages   json NOT NULL
);

CREATE EXTENSION IF NOT EXISTS pgroonga;

CREATE TABLE japanese_indices
(
    japanese_id INTEGER PRIMARY KEY,
    linking     TEXT
);

CREATE TABLE sentences
(
    sentence_id INTEGER PRIMARY KEY,
    sentence    TEXT    NOT NULL,
    language    CHAR(3) NOT NULL
);

CREATE TABLE links_japanese_translation
(
    japanese_id    INTEGER NOT NULL REFERENCES sentences (sentence_id),
    translation_id INTEGER NOT NULL REFERENCES sentences (sentence_id),
    PRIMARY KEY (japanese_id, translation_id)
);
