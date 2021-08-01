CREATE TABLE City (
  id LONG PRIMARY KEY, name VARCHAR)
  WITH "template=replicated";

CREATE TABLE Person (
  id LONG, name VARCHAR, city_id LONG, PRIMARY KEY (id, city_id))
  WITH "backups=1, affinityKey=city_id";

CREATE INDEX idx_city_name ON City (name);

CREATE INDEX idx_person_name ON Person (name);

INSERT INTO City (id, name) VALUES (1, 'Beijing');
INSERT INTO City (id, name) VALUES (2, 'Shanghai');
INSERT INTO City (id, name) VALUES (3, 'Taiyuan');

INSERT INTO Person (id, name, city_id) VALUES (1, 'Jeffrey', 3);
INSERT INTO Person (id, name, city_id) VALUES (2, 'Jane', 2);
INSERT INTO Person (id, name, city_id) VALUES (3, 'Mary', 1);
INSERT INTO Person (id, name, city_id) VALUES (4, 'Richard', 2);

SELECT * FROM City;

SELECT name FROM City WHERE id = 1;

SELECT p.name, c.name
FROM Person p, City c
WHERE p.city_id = c.id;

UPDATE City
SET name = 'Shenzhen'
WHERE id = 2;

DELETE FROM Person
WHERE name = 'Mary';