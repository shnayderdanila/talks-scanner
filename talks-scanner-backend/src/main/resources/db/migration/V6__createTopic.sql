CREATE SEQUENCE topic_seq START 1 INCREMENT 50;

CREATE TABLE topic (
                      id BIGSERIAL NOT NULL,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(70) NOT NULL,
                      description VARCHAR(550) NOT NULL,
                      tags VARCHAR(250),
                      event_date TIMESTAMP,
                      presentation_link VARCHAR(70),
                      video_link VARCHAR(70),
                      status VARCHAR(15) NOT NULL,
                      user_id INT8 NOT NULL,
                      PRIMARY KEY (id),
                      FOREIGN KEY (user_id) REFERENCES "user"(id)
);
