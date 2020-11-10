CREATE TABLE IF NOT EXISTS japaneses
(
    japanese_id SERIAL PRIMARY KEY,
    kanji       TEXT,
    kana        TEXT
);

CREATE TABLE IF NOT EXISTS translations
(
    translation_id SERIAL PRIMARY KEY,
    japanese_id    INTEGER NOT NULL REFERENCES japaneses (japanese_id),
    locale         TEXT    NOT NULL,
    input          TEXT    NOT NULL,
    sort_letter    CHAR(1) NOT NULL,
    details        TEXT,
    example        TEXT,
    tags           TEXT ARRAY
);
