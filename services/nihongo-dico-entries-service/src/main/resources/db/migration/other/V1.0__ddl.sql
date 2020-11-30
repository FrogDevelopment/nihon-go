CREATE TABLE glosses
(
    sense_seq  TEXT NOT NULL REFERENCES jpn.senses (sense_seq),
    vocabulary TEXT NOT NULL
);
