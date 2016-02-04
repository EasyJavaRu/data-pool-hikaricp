CREATE ROLE test WITH PASSWORD 'test';
ALTER ROLE test WITH LOGIN;

CREATE DATABASE test OWNER test;

GRANT CREATE ON DATABASE test TO test;