INSERT INTO users (email, password, name, surname, phone, city, rating, role, activity)
VALUES ('admin', '$2a$10$rWFD3BMbMcOE2TskZNVzQO9U4.nRenti5/zuFc6rThTfzZPTMx0li',
        'Admin', 'Admin', '0123456789', 'Москва', 0, 'ADMIN', true),
       ('mail@mail.ru', '$2a$10$ESA8kkc91VoK1V/N/jT2luZNobYPXVzwJdVvq53O3aOwcnr/1qQ9m',
        'Михаил', 'Егоров', '9032276404', 'Москва', 2, 'USER', true),
       ('mail@mail.com', '$2a$10$pmNufdtB89zaQJXCn0Mn/e4egO1i8Gc7TVV7Ey4Cf2GK3LScdDUGC',
        'Елена', 'Павлова', '9972340128', 'Санкт-Петербург', 0, 'USER', true);

INSERT INTO posts (user_id, title, description, price, promotion, sold, posting_date, category, seller_rating, city, exchanged)
VALUES (3, 'Apple Iphone 13 256 gb', 'Абсолютно новый Айфон, оказался ненужен',
        125500.00, 1000.00, false, '2022-09-08', 'ELECTRONICS', 0, 'Санкт-Петербург', true),
       (2, 'Apple Iphone 11 Pro 128 gb', 'Айфон 11 про, был в использовании полтора года, аккумулятор изношен всего на 7%, физических повреждений нет',
        52000.00, 8000.00, false, '2022-09-09', 'ELECTRONICS', 2, 'Москва', false),
       (2, 'Xbox Series S 512gb', 'Младшая консоль нового поколения от Microsoft, в идеальном состоянии',
        23500.00, 0, true, '2022-09-09', 'ELECTRONICS', 2, 'Москва', false),
       (2, 'Xiaomi Redmi Buds 4 Pro', 'Неплохие TWS-ки, пользовалась полгода. Решила обновиться до яблочных.',
        2800.00, 1000.00, false, '2022-09-11', 'ELECTRONICS', 2, 'Москва', true),
       (3, 'Пальто Top Man M', 'Мужское пальто размера М, в нормальном состоянии',
        4200.00, 0, false, '2022-09-12', 'CLOTHES', 0, 'Санкт-Петербург', false);

INSERT INTO messages (sender_id, receiver_id, content, time)
VALUES (2, 3, 'Здравствуйте, хотел бы узнать у вас о возможности снижения цены на Айфон 13',
        '2022-09-08 14:47:13.270377'),
       (3, 2, 'Могу рассмотреть вариант снижения цены на сумму не больше 5 тысяч рублей' ,
        '2022-09-08 14:49:02.327292');