-- ADMIN -- login: admin, pass: admin
-- SUBSCRIBER -- login: subscriber, pass: 123456
-- USER -- login: userr, pass: qwerty
-- LOCKED -- login: locked, pass: qwerty
-- DELETED -- login: deleted, pass: qwerty
INSERT INTO USERS (username, fullname, bankaccount, password, role)
VALUES
        ('admin', 'Admin Adminski', 99999, '$2a$10$hW7TdoUPWhEdprtj0XjCJ.iDopWV2UUaHWp2l7FoK3mRbLTX7W81q', 'ROLE_ADMIN'),
        ('subscriber', 'Vladimir Kostin', 435, '$2a$10$Q7vEKw6rzmpNulORJMheF./2QAjAq15fGzCoSoZgSj02jHaOITxRi', 'ROLE_SUBSCRIBER'),
        ('userr', 'Anton Pavlov', 582, '$2a$10$fPK7rqD0QjOBVGp9YdxnfOxBBX2ZSdiNp3wCp1nlLIxQf6EAaOlsK', 'ROLE_USER'),
        ('locked', 'Pavel Zhdanov', -45, '$2a$10$fPK7rqD0QjOBVGp9YdxnfOxBBX2ZSdiNp3wCp1nlLIxQf6EAaOlsK', 'ROLE_LOCKED'),
        ('deleted', 'Arnd Peiffer', 500, '$2a$10$fPK7rqD0QjOBVGp9YdxnfOxBBX2ZSdiNp3wCp1nlLIxQf6EAaOlsK', 'ROLE_DELETED');

--
INSERT INTO SERVICES (cost, duration, name, description)
VALUES
        (40, 2, 'Интеллектуальный диктофон',
        'Не думайте, забудете Вы информацию или нет — просто наберите Вашего помощника! ' ||
        'Мы запишем текст под диктовку и перешлём Вам на почту.'),
        (130, 5, 'Ведение телефонных переговоров по Вашему поручению',
         'Позвоним • дозвонимся в самые недоступные организации ' ||
         '• договоримся и добьёмся того, что Вам необходимо.');

INSERT INTO USER_SERVICES (user_id, service_id) VALUES (1, 1), (1, 2), (2, 1), (2, 2);

INSERT INTO BILLS (actual_cost, end_date, is_paid, start_date, service_unit_id, user_id)
VALUES
        (130,	'2018-02-05',	TRUE,	'2018-01-31',	2,	1),
        (40,	'2018-02-02',	FALSE,	'2018-01-31',	1,	1),
        (50, '2018-01-23', FALSE, '2018-01-25', 1, 2),
        (100, '2017-09-10', TRUE, '2017-09-15', 2, 2);