CREATE TABLE actions
(
    uuid     uuid PRIMARY KEY,
    username TEXT NOT NULL REFERENCES users (username),
    action   TEXT NOT NULL
);
