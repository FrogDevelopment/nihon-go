-- remove previous authorities
DELETE
FROM authorities
WHERE username = 'admin';

-- now admin is a SUPER_ADMIN
INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_SUPER_ADMIN');
