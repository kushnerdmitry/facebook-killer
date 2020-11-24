CREATE TABLE IF NOT EXISTS users (
  id          SERIAL    PRIMARY KEY NOT NULL,
  first_name  TEXT                  NOT NULL,
  second_name TEXT                  NOT NULL,
  age         INTEGER               NOT NULL
);

INSERT INTO users (first_name, second_name, age) values ('Дмитрий',  'Кушнер', 21);
INSERT INTO users (first_name, second_name, age) values ('Иван', 'Андреев', 31);
INSERT INTO users (first_name, second_name, age) values ('Полина', 'Иванова', 43);
INSERT INTO users (first_name, second_name, age) values ('Ольга', 'Ольговна', 56);
INSERT INTO users (first_name, second_name, age) values ('Артур', 'Артуров', 9);
INSERT INTO users (first_name, second_name, age) values ('Гарри', 'Поттер', 54);
INSERT INTO users (first_name, second_name, age) values ('Илон', 'Маск', 32);
INSERT INTO users (first_name, second_name, age) values ('Стив', 'Джобс', 12);
INSERT INTO users (first_name, second_name, age) values ('Олег', 'Великий', 34);
INSERT INTO users (first_name, second_name, age) values ('Андрей', 'Андреев', 45);
INSERT INTO users (first_name, second_name, age) values ('Светлана', 'Вяткова', 32);
INSERT INTO users (first_name, second_name, age) values ('Диана', 'Прицесса', 24)

