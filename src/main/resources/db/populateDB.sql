DELETE FROM user_role;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES (to_timestamp('01.06.2015 14:00', 'dd.mm.yyyy hh24:mi'), 'Админ ланч', 510, 100001),
       (to_timestamp('01.06.2015 20:00', 'dd.mm.yyyy hh24:mi'), 'Админ ужин', 610, 100001),
       (to_timestamp('30.01.2020 10:00', 'dd.mm.yyyy hh24:mi'), 'Пользователь Завтрак', 1456, 100000),
       (to_timestamp('30.01.2020 13:00', 'dd.mm.yyyy hh24:mi'), 'Пользователь Обед', 1234, 100000),
       (to_timestamp('30.01.2020 20:00', 'dd.mm.yyyy hh24:mi'), 'Пользователь Ужин', 321, 100000),
       (to_timestamp('31.01.2020 11:33', 'dd.mm.yyyy hh24:mi'), 'Пользователь Завтрак', 453, 100000),
       (to_timestamp('31.01.2020 12:45', 'dd.mm.yyyy hh24:mi'), 'Пользователь Обед', 1500, 100000),
       (to_timestamp('31.01.2020 21:15', 'dd.mm.yyyy hh24:mi'), 'Пользователь Ужин', 200, 100000);