CREATE TABLE about
(
    about_id    SERIAL PRIMARY KEY,
    jmdict_date TEXT    NOT NULL,
    nb_entries  NUMERIC NOT NULL,
    languages   json    NOT NULL
);

CREATE EXTENSION pgroonga;
