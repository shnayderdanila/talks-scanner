CREATE TABLE "user" (

    id          bigserial   NOT NULL,
    email       VARCHAR(70) NOT NULL,
    firstname   VARCHAR(70) NOT NULL,
    lastname    VARCHAR(70) NOT NULL,
    login       VARCHAR(70) NOT NULL,
    path_logo   VARCHAR(256),
    sex         VARCHAR(6)  NOT NULL,

    UNIQUE(email),
    UNIQUE(login),
    PRIMARY KEY (id)
);