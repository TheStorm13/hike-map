CREATE TABLE user_hike_like
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER NOT NULL REFERENCES user_account (id),
    hike_id    INTEGER NOT NULL REFERENCES hike (id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);