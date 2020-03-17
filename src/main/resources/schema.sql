DROP TABLE if exists tasks;
CREATE TABLE tasks (id SERIAL PRIMARY KEY AUTO_INCREMENT, title varchar(255) not null, description varchar(255) not null, expected_date timestamp not null, completed boolean);

INSERT INTO tasks (title, description, expected_date, completed) VALUES ('Excercise', 'Do excersice in the park', '2020-03-20', false);
INSERT INTO tasks (title, description, expected_date, completed) VALUES ('Programming my own website', 'Create a blog to post cool topics', '2020-03-20', false);
