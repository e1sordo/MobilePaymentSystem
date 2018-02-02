-- ADMIN -- login: admin, pass: admin
-- SUBSCRIBER -- login: subscriber, pass: 123456
-- USER -- login: userr, pass: qwerty
-- LOCKED -- login: locked, pass: qwerty
-- DELETED -- login: deleted, pass: qwerty
INSERT INTO USERS (username, fullname, bankaccount, password, role)
VALUES
        ('admin', 'Vladimir Kostin', 99999, '$2a$10$hW7TdoUPWhEdprtj0XjCJ.iDopWV2UUaHWp2l7FoK3mRbLTX7W81q', 'ROLE_ADMIN'),
        ('mikey', 'Marsel Allaiarov', 435, '$2a$10$fPK7rqD0QjOBVGp9YdxnfOxBBX2ZSdiNp3wCp1nlLIxQf6EAaOlsK', 'ROLE_SUBSCRIBER'),
        ('stranger', 'Bill Gates', 107, '$2a$10$fPK7rqD0QjOBVGp9YdxnfOxBBX2ZSdiNp3wCp1nlLIxQf6EAaOlsK', 'ROLE_LOCKED'),
        ('exceptional', 'Gryu Felonius', 0, '$2a$10$fPK7rqD0QjOBVGp9YdxnfOxBBX2ZSdiNp3wCp1nlLIxQf6EAaOlsK', 'ROLE_DELETED');

--
INSERT INTO SERVICES (cost, duration, name, description)
VALUES
        (40, 2, 'Интеллектуальный диктофон',
        'Не думайте, забудете Вы информацию или нет — просто наберите Вашего помощника! ' ||
        'Мы запишем текст под диктовку и перешлём Вам на почту.'),
        (130, 5, 'Ведение телефонных переговоров по Вашему поручению',
         'Позвоним • дозвонимся в самые недоступные организации ' ||
         '• договоримся и добьёмся того, что Вам необходимо.'),
         (200, 30, 'Красивый телефонный звонок',
         'Подарите звонящему минутку Шопена.');

INSERT INTO USER_SERVICES (user_id, service_id) VALUES (2, 1), (2, 2), (2, 3);

INSERT INTO BILLS (actual_cost, is_paid, start_date, end_date, service_unit_id, user_id)
VALUES
        (40, TRUE, '2017-02-01', '2017-02-03', 1, 2),
        (100, TRUE, '2018-02-01', '2018-02-06', 2, 2),
        (200, FALSE, '2018-01-31', '2018-03-01', 3, 2);