CREATE TABLE entries
(
    entry_seq INTEGER PRIMARY KEY,
    kanji     TEXT,
    reading   TEXT NOT NULL
);

CREATE TABLE senses
(
    sense_seq TEXT PRIMARY KEY,
    entry_seq INTEGER NOT NULL REFERENCES entries (entry_seq),
    pos       VARCHAR(50),
    field     VARCHAR(20),
    misc      VARCHAR(20),
    info      VARCHAR(250),
    dial      VARCHAR(20)
);

CREATE TABLE glosses
(
    sense_seq  TEXT    NOT NULL REFERENCES senses (sense_seq),
    lang       CHAR(3) NOT NULL,
    vocabulary TEXT    NOT NULL
);

CREATE TABLE about
(
    about_id    SERIAL PRIMARY KEY,
    jmdict_date TEXT    NOT NULL,
    nb_entries  NUMERIC NOT NULL,
    languages   json    NOT NULL
);

CREATE EXTENSION pgroonga;
