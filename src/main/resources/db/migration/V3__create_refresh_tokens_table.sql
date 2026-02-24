CREATE TABLE refresh_tokens (
                                id SERIAL PRIMARY KEY,
                                token_value VARCHAR(255),
                                user_id BIGINT NOT NULL REFERENCES users(id),
                                revoked BOOLEAN DEFAULT FALSE,
                                creation_date TIMESTAMP,
                                expiry_date TIMESTAMP,
                                jwt_id VARCHAR(255)
);