INSERT INTO role (name)
VALUES ('guest'),
       ('member'),
       ('organizer'),
       ('admin')
    ON CONFLICT (name) DO NOTHING;