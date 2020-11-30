CREATE TABLE entries
(
    entry_seq INTEGER PRIMARY KEY,
    kanji     TEXT,
    kana      TEXT,
    reading   TEXT NOT NULL
);

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

