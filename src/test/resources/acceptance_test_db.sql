DROP TABLE station IF EXISTS;
DROP TABLE line IF EXISTS;

CREATE TABLE station
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE line
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL,
    PRIMARY KEY(id)
);