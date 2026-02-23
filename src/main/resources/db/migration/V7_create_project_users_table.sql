CREATE TABLE project_users (
                               project_id BIGINT NOT NULL REFERENCES projects(id),
                               user_id BIGINT NOT NULL REFERENCES users(id),
                               PRIMARY KEY (project_id, user_id)
);