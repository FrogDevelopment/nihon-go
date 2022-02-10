CREATE TABLE IF NOT EXISTS japaneses
(
    japanese_id SERIAL PRIMARY KEY,
    kanji       TEXT,
    kana        TEXT     NOT NULL,
    lesson      SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS translations
(
    translation_id SERIAL PRIMARY KEY,
    japanese_id    INTEGER NOT NULL REFERENCES japaneses (japanese_id),
    locale         TEXT    NOT NULL,
    input          TEXT    NOT NULL,
    sort_letter    CHAR(1) NOT NULL,
    details        TEXT,
    example        TEXT
);

CREATE TABLE IF NOT EXISTS lessons
(
    lesson          SMALLINT PRIMARY KEY,
    update_datetime TIMESTAMP NOT NULL DEFAULT NOW(),
    export_datetime TIMESTAMP
)
