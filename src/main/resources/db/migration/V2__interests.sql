CREATE TABLE interests (
  id          SERIAL    PRIMARY KEY NOT NULL,
  name        TEXT      UNIQUE      NOT NULL
);


INSERT INTO interests (name) values ('Кино');
INSERT INTO interests (name) values ('Спорт');
INSERT INTO interests (name) values ('Программирование');
INSERT INTO interests (name) values ('Чтение');
INSERT INTO interests (name) values ('Театр');
INSERT INTO interests (name) values ('Танцы');
INSERT INTO interests (name) values ('Кулинария');





