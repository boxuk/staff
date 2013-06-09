# --- !Ups

ALTER TABLE employees ADD role_id integer DEFAULT 1;

# --- !Downs

ALTER TABLE employees DROP role_id;


