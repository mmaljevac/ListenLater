CREATE TABLE USERS
(
    ID INT GENERATED ALWAYS AS IDENTITY,
    USERNAME VARCHAR(50) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    IS_ADMIN BOOLEAN NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ALBUMS
(
    ID INT GENERATED ALWAYS AS IDENTITY,
    NAME VARCHAR(100) NOT NULL,
    ARTIST VARCHAR(100) NOT NULL,
    IMG_URL VARCHAR(100) NOT NULL,
    ID_USER INT,
    PRIMARY KEY (ID)
);