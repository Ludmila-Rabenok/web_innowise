DROP TABLE order_procedure;
DROP table orders;
DROP TABLE procedures;
DROP TABLE users;
DROP TYPE role_enum;
DROP TYPE status_enum;

CREATE TYPE role_enum AS ENUM ('CLIENT', 'ADMIN');

CREATE TYPE status_enum AS ENUM ('APPROVED', 'MODERATION', 'REJECTED');

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    password varchar(255) NOT NULL,
    role     role_enum    NOT NULL DEFAULT 'CLIENT'
);

CREATE TABLE procedures
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255)   NOT NULL,
    price          NUMERIC(10, 2) NOT NULL,
    rating_average NUMERIC(10, 2) default NULL CHECK (rating_average IS NULL OR (rating_average >= 1 AND rating_average <= 5)),
    rating_count   INT            default 0
);

CREATE TABLE orders
(
    id        SERIAL PRIMARY KEY,
    user_id   INT         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    lead_time TIMESTAMP   NOT NULL,
    status    status_enum NOT NULL DEFAULT 'MODERATION',
    bill      NUMERIC(10, 2)
);

CREATE TABLE order_procedure
(
    order_id     INT NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    procedure_id INT NOT NULL REFERENCES procedures (id) ON DELETE CASCADE,
    PRIMARY KEY (order_id, procedure_id)
);

INSERT INTO users (name, password, role)
VALUES ('LudaTest', 'Password_Luda_Test', 'ADMIN'),
       ('IrinaTest', 'Password_Irina_Test', 'CLIENT'),
       ('OlegTest', 'Password_Oleg_Test', 'CLIENT');

INSERT INTO procedures (name, price)
VALUES ('haircut', 15.00),
       ('nails', 20.00),
       ('wash', 50.00);

INSERT INTO orders (user_id, lead_time, status, bill)
VALUES (2, '2026-01-06 10:00:00', 'MODERATION', 35.00),
       (2, '2026-01-07 14:00:00', 'APPROVED', 50.00),
       (3, '2026-01-08 12:00:00', 'REJECTED', 40.00);

INSERT INTO order_procedure (order_id, procedure_id)
VALUES (1, 1),
       (1, 2);
INSERT INTO order_procedure (order_id, procedure_id)
VALUES (2, 3);
INSERT INTO order_procedure (order_id, procedure_id)
VALUES (3, 3);
