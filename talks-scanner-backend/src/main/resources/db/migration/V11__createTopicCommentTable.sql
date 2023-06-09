CREATE SEQUENCE seq_topic_comment START 1 INCREMENT 50;

CREATE TABLE topic_comment (
                               id bigint NOT NULL,
                               creation_date TIMESTAMP NOT NULL,
                               last_update_date TIMESTAMP NOT NULL,
                               "text" varchar(2000) NOT NULL,
                               user_id int8 NOT NULL,
                               topic_id int8 NOT NULL,
                               PRIMARY KEY (id),
                               FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
                               FOREIGN KEY (topic_id) REFERENCES topic(id) ON DELETE CASCADE
);