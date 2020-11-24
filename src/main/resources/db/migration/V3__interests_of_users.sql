CREATE TABLE interests_of_users (
  id          SERIAL       UNIQUE   NOT NULL,
  user_id     SERIAL                NOT NULL,
  interest_id SERIAL                NOT NULL,
  estimate    INTEGER               NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  FOREIGN KEY (interest_id) REFERENCES interests (id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, interest_id)
);

INSERT INTO interests_of_users (user_id, interest_id, estimate) values (1, 1, 30);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (1, 2, 10);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (1, 3, 5);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (1, 4, 90);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (2, 5, 34);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (2, 6, 29);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (2, 7, 56);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (2, 2, 12);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (3, 1, 78);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (3, 2, 34);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (3, 3, 45);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (3, 4, 56);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (3, 5, 67);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (4, 3, 12);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (4, 4, 34);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (4, 5, 57);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (4, 6, 90);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (4, 7, 12);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (4, 1, 32);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (5, 1, 56);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (6, 2, 89);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (7, 3, 34);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (7, 5, 78);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (8, 6, 4);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (8, 7, 23);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (9, 3, 89);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (9, 2, 55);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (10, 1, 44);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (10, 2, 22);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (11, 5, 34);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (11, 6, 56);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (12, 7, 76);
INSERT INTO interests_of_users (user_id, interest_id, estimate) values (12, 2, 93);




