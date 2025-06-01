CREATE TABLE user_account
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash CHAR(60)            NOT NULL,
    role_id       INTEGER             NOT NULL REFERENCES Role (id) DEFAULT 1,
    created_at    TIMESTAMP WITH TIME ZONE                          DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE                          DEFAULT CURRENT_TIMESTAMP
);
