INSERT INTO entries(entry_seq, kanji, kana, reading)
VALUES (1, 'kanji_1', 'kana_1', 'reading_1'),
       (2, 'kanji_2', 'kana_2', 'reading_2');
INSERT INTO senses(sense_seq, entry_seq)
VALUES ('123', 1),
       ('456', 2);
INSERT INTO glosses(sense_seq, lang, vocabulary)
VALUES ('123', 'fra', 'vocabulary_fra_1'),
       ('456', 'fra', 'vocabulary_fra_2'),
       ('123', 'eng', 'vocabulary_eng'),
       ('123', 'dut', 'vocabulary_dut_1'),
       ('456', 'dut', 'vocabulary_dut_2'),
       ('123', 'rus', 'vocabulary_rus'),
       ('123', 'slv', 'vocabulary_slv');
