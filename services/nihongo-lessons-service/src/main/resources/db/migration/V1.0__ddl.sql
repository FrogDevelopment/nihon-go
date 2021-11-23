CREATE TABLE IF NOT EXISTS japaneses
(
    japanese_id SERIAL PRIMARY KEY,
    kanji       TEXT,
    kana        TEXT,
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

CREATE TABLE IF NOT EXISTS exportable_lessons
(
    lesson          SMALLINT  NOT NULL,
    locale          TEXT      NOT NULL,
    exportable      BOOLEAN DEFAULT FALSE,
    update_datetime TIMESTAMP NOT NULL
)