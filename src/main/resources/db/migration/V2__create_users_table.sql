CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       user_name VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       name VARCHAR(255),
                       surname VARCHAR(255)
);