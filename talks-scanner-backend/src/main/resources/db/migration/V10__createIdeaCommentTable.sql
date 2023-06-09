CREATE SEQUENCE seq_idea_comment START 1 INCREMENT 50;

CREATE TABLE idea_comment (
                              id bigint NOT NULL,
                              creation_date TIMESTAMP NOT NULL,
                              last_update_date TIMESTAMP NOT NULL,
                              "text" varchar(2000) NOT NULL,
                              user_id int8 NOT NULL,
                              idea_id int8 NOT NULL,
                              PRIMARY KEY (id),
                              FOREIGN KEY (idea_id) REFERENCES idea(id) ON DELETE CASCADE,
                              FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);