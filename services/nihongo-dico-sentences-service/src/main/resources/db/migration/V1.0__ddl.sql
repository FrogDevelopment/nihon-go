CREATE TABLE sentences
(
    sentence_id INTEGER PRIMARY KEY,
    lang        CHAR(3) NOT NULL,
    sentence    TEXT    NOT NULL
);

CREATE TABLE links_japanese_translation
(
    japanese_id    INTEGER NOT NULL REFERENCES sentences (sentence_id),
    translation_id INTEGER NOT NULL REFERENCES sentences (sentence_id),
    PRIMARY KEY (japanese_id, translation_id)
);

CREATE TABLE japanese_indices
(
    japanese_id INTEGER PRIMARY KEY,
    linking     TEXT
);

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

CREATE TABLE about
(
    about_id    SERIAL PRIMARY KEY,
    date_import TEXT    NOT NULL,
    nb_entries  NUMERIC NOT NULL,
    languages   json    NOT NULL
);

CREATE EXTENSION pgroonga;
