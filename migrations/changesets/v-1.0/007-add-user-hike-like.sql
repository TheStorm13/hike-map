CREATE TABLE user_hike_like
(
    user_id    INTEGER NOT NULL REFERENCES user_account (id),
    hike_id    INTEGER NOT NULL REFERENCES hike (id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, hike_id)
);