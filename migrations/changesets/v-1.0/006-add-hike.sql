CREATE
EXTENSION IF NOT EXISTS postgis;

CREATE TABLE hike
(
    id              SERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    photo_path      VARCHAR(255),
    start_date      DATE         NOT NULL,
    end_date        DATE         NOT NULL,
    track_gpx_path  VARCHAR(255),
    track_geometry  geometry(LineString, 4326),
    report_pdf_path VARCHAR(255),
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    organizer_id    INTEGER      NOT NULL REFERENCES user_account (id),
    area_id         INTEGER      NOT NULL REFERENCES area (id),
    difficulty      INTEGER      NOT NULL,
    is_categorical  BOOLEAN      DEFAULT FALSE,
    hike_type_id    INTEGER      NOT NULL REFERENCES hike_type (id),
    CONSTRAINT valid_dates CHECK (end_date >= start_date)
);
