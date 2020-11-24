CREATE TABLE groups (
  id          SERIAL    PRIMARY KEY NOT NULL,
  name        TEXT                  NOT NULL,
  description TEXT                  NOT NULL,
  admin       SERIAL,
  FOREIGN KEY (admin) REFERENCES users (id) ON DELETE SET NULL
);

INSERT INTO groups (name, description, admin) values ('Футбол', 'Все о футболе', 1);
INSERT INTO groups (name, description, admin) values ('Волейбол', 'Все о волейболе', 2);
INSERT INTO groups (name, description, admin) values ('Хоккей', 'Все о хоккее', 4);
INSERT INTO groups (name, description, admin) values ('Кулинария', 'Рецептики', 5);
INSERT INTO groups (name, description, admin) values ('Новости', 'Мировые новости', 7);
INSERT INTO groups (name, description, admin) values ('Погода', 'Погода в России', 11);
