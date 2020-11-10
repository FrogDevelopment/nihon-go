CREATE TABLE users
(
    username TEXT PRIMARY KEY,
    password TEXT    NOT NULL,
    enabled  BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE authorities
(
    username  TEXT NOT NULL REFERENCES users (username),
    authority TEXT NOT NULL
);

CREATE TABLE revoked_token
(
    jti                 TEXT                     NOT NULL,
    revocation_datetime TIMESTAMP WITH TIME ZONE NOT NULL
);
