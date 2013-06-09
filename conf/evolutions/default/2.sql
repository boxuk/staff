# --- !Ups

ALTER TABLE employees ADD role_id integer;

# --- !Downs

ALTER TABLE employees DROP role_id;


