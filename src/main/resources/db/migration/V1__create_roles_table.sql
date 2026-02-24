CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       role_type VARCHAR(50) UNIQUE NOT NULL
);