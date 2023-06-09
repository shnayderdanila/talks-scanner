CREATE SEQUENCE seq_idea START 1 INCREMENT 50;

CREATE TABLE idea (
    id BIGSERIAL NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(550) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    last_update_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    "user_id" INT8 NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "user"(id),
    PRIMARY KEY (id)
);