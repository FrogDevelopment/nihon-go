CREATE TABLE sentences
(
    sentence_id INTEGER PRIMARY KEY,
    sentence    TEXT NOT NULL
);

CREATE TABLE japanese_indices
(
    japanese_id INTEGER PRIMARY KEY,
    linking     TEXT
);
