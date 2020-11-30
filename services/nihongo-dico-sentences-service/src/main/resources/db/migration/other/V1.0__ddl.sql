CREATE TABLE sentences
(
    sentence_id INTEGER PRIMARY KEY,
    sentence    TEXT NOT NULL
);

CREATE TABLE links_japanese_translation
(
    japanese_id    INTEGER NOT NULL REFERENCES jpn.sentences (sentence_id),
    translation_id INTEGER NOT NULL REFERENCES sentences (sentence_id),
    PRIMARY KEY (japanese_id, translation_id)
);
