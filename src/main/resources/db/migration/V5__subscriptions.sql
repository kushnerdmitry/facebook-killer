CREATE TABLE users_subscriptions (
  id            SERIAL        UNIQUE  NOT NULL,
  user_from_id  SERIAL                NOT NULL,
  user_to_id    SERIAL                NOT NULL,
  FOREIGN KEY (user_from_id) REFERENCES users (id) ON DELETE CASCADE,
  FOREIGN KEY (user_to_id)   REFERENCES users (id) ON DELETE CASCADE,
  PRIMARY KEY (user_from_id, user_to_id)
);

CREATE TABLE groups_subscriptions (
  id            SERIAL        UNIQUE  NOT NULL,
  user_from_id  SERIAL                NOT NULL,
  group_to_id   SERIAL                NOT NULL,
  FOREIGN KEY (user_from_id) REFERENCES users (id)  ON DELETE CASCADE,
  FOREIGN KEY (group_to_id)  REFERENCES groups (id) ON DELETE CASCADE,
  PRIMARY KEY (user_from_id, group_to_id)
);

INSERT INTO users_subscriptions (user_from_id, user_to_id) values (1, 2);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (1, 3);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (1, 12);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (2, 1);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (2, 4);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (2, 5);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (2, 6);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (2, 7);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (2, 8);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (3, 11);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (3, 12);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (3, 1);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (3, 7);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (3, 2);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (3, 4);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (4, 1);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (4, 2);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (4, 3);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (4, 7);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (5, 9);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (5, 10);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (5, 12);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (5, 6);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (6, 1);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (6, 2);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (6, 3);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (7, 4);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (7, 5);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (8, 6);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (10, 7);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (10, 8);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (10, 9);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (12, 10);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (12, 11);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (12, 7);
INSERT INTO users_subscriptions (user_from_id, user_to_id) values (12, 8);



INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (1, 1);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (2, 2);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (3, 3);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (4, 4);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (5, 5);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (6, 6);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (7, 1);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (8, 2);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (9, 3);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (10, 4);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (11, 5);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (12, 6);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (3, 1);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (4, 2);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (5, 3);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (6, 4);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (7, 5);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (8, 6);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (9, 1);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (10, 2);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (11, 3);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (12, 4);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (1, 5);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (2, 6);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (3, 2);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (4, 3);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (5, 4);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (6, 5);
INSERT INTO groups_subscriptions (user_from_id, group_to_id) values (7, 6);


