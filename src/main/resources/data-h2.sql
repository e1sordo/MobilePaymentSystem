-- pass: admin
INSERT INTO USERS (username, fullname, bankbook, password, role)
VALUES ('admin', 'Admin Adminski', 99999, '$2a$10$hW7TdoUPWhEdprtj0XjCJ.iDopWV2UUaHWp2l7FoK3mRbLTX7W81q', 'ROLE_ADMIN');

-- pass: 123456
INSERT INTO USERS (username, fullname, bankbook, password, role)
VALUES ('vlavik', 'Vladimir Kostin', 435, '$2a$10$Q7vEKw6rzmpNulORJMheF./2QAjAq15fGzCoSoZgSj02jHaOITxRi', 'ROLE_SUBSCRIBER');

-- pass: qwerty
INSERT INTO USERS (username, fullname, bankbook, password, role)
VALUES ('boy', 'Anton Pavlov', 129, '$2a$10$fPK7rqD0QjOBVGp9YdxnfOxBBX2ZSdiNp3wCp1nlLIxQf6EAaOlsK', 'ROLE_USER');

--
INSERT INTO SERVICES (cost, name, description)
VALUES (40, 'Интеллектуальный диктофон',
        'Не думайте, забудете Вы информацию или нет — просто наберите Вашего помощника! Мы запишем текст под диктовку и перешлём Вам на почту.');

INSERT INTO SERVICES (cost, name, description)
VALUES (130, 'Ведение телефонных переговоров по Вашему поручению',
        'Позвоним • дозвонимся в самые недоступные организации • договоримся и добьёмся того, что Вам необходимо.');

