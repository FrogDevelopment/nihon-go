CREATE TABLE about
(
    about_id    SERIAL PRIMARY KEY,
    jmdict_date TEXT    NOT NULL,
    nb_entries  INTEGER NOT NULL,
    languages   json    NOT NULL
);

CREATE EXTENSION IF NOT EXISTS pgroonga;

CREATE TABLE entries
(
    entry_seq INTEGER PRIMARY KEY,
    kanji     TEXT,
    kana      TEXT,
    reading   TEXT NOT NULL
);

CREATE UNIQUE INDEX index_entries_entry_seq ON entries (entry_seq);

CREATE TABLE senses
(
    sense_seq TEXT PRIMARY KEY,
    entry_seq INTEGER NOT NULL REFERENCES entries (entry_seq),
    pos       VARCHAR(50),
    field     VARCHAR(50),
    misc      VARCHAR(50),
    info      VARCHAR(250),
    dial      VARCHAR(50)
);

CREATE UNIQUE INDEX index_senses_sense_seq ON senses (sense_seq);

CREATE TABLE glosses
(
    sense_seq  TEXT    NOT NULL REFERENCES senses (sense_seq),
    language   CHAR(3) NOT NULL,
    vocabulary TEXT    NOT NULL
);
