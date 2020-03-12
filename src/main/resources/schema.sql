CREATE TABLE tasks (id SERIAL PRIMARY KEY AUTO_INCREMENT, title varchar(255) not null, description varchar(255) not null, expected_date date not null, completed boolean);

