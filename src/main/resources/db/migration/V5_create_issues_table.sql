CREATE TABLE issues (
                        id SERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        issue_status VARCHAR(50),
                        project_id BIGINT NOT NULL REFERENCES projects(id),
                        user_id BIGINT REFERENCES users(id),
                        archived BOOLEAN DEFAULT FALSE,
                        archived_at TIMESTAMP
);