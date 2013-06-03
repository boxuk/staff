# --- !Ups

CREATE SEQUENCE employees_id_seq;

CREATE TABLE employees (
  id         INTEGER NOT NULL DEFAULT nextval('employees_id_seq') PRIMARY KEY,
  first_name VARCHAR(255),
  last_name  VARCHAR(255),
  email      VARCHAR(255),
  phone      VARCHAR(255),
  website    VARCHAR(255),
  bio        VARCHAR(255),
  UNIQUE (email)
);

# --- !Downs

DROP TABLE employees;
DROP SEQUENCE employees_id_seq;