INSERT INTO categories (name) VALUES ('Электроника'), ('Одежда и аксессуары'), ('Товары для дома'),
('Услуги'), ('Вакансии'), ('Транспорт'), ('Недвижимость');
INSERT INTO users (email, password, name, surname, phone, city, registration_date, rating, role, activity)
VALUES ('admin@mail.com', '$2a$12$WeV.ei.t4wH8tHhNiB3B2uhg3C6/YhX6Rff4.aTy9eclvp0ZyrvyO',
        'Admin', 'Admin', '0123456789', 'Москва', '2022-09-04', 0, 'ADMIN', true),
       ('mail@mail.ru', '$2a$10$ESA8kkc91VoK1V/N/jT2luZNobYPXVzwJdVvq53O3aOwcnr/1qQ9m',
        'Михаил', 'Егоров', '9032276404', 'Москва', '2022-09-06', 4, 'USER', true),
       ('mail@mail.com', '$2a$12$9a.5AH.FTtFwq67xt4Xj/O3a3xPMijMNPgPB1F1mFn25uEFR8Upxm',
        'Елена', 'Павлова', '9972340128', 'Санкт-Петербург', '2022-09-06', 0, 'USER', true),
       ('another@mail.com', '$2a$12$EUBdHgNFPmUC/AQBXUhTv.GAsTNQUQN2QSQDwKTohh8zjtMPU0roe',
          'Григорий', 'Дятлов', '9149654221', 'Волгоград', '2022-11-03', 0, 'USER', true),
       ('something@mail.ru', '$2a$12$si4SNPWTchsFk2vkpNj0YOWea3PB4k2JpK6nggzFz1ErfYlDZ9bey',
          'Евгений', 'Воронов', '556140871', 'Москва', '2022-10-26', 3.5, 'USER', true);

INSERT INTO posts (user_id, title, description, price, promotion, sold,
                   posting_date, category_id, seller_rating, city, exchanged, delivered, buyer_id)
VALUES (3, 'Apple Iphone 13 256 gb', 'Абсолютно новый Айфон, оказался ненужен',
        125500.00, 1000.00, false, '2022-09-08', 1, 0, 'Санкт-Петербург', true, false, 0),
       (2, 'Apple Iphone 11 Pro 128 gb', 'Айфон 11 про, был в использовании полтора года, аккумулятор изношен всего на 7%, физических повреждений нет',
        52000.00, 8000.00, false, '2022-09-09', 1, 4, 'Москва', false, false, 0),
       (2, 'Xbox Series S 512gb', 'Младшая консоль нового поколения от Microsoft, в идеальном состоянии',
        23500.00, 0, true, '2022-09-09', 1, 4, 'Москва', false, true, 3),
       (5, 'Xiaomi Redmi Buds 4 Pro', 'Неплохие TWS-ки, пользовалась полгода. Решила обновиться до яблочных.',
        2800.00, 1000.00, true, '2022-11-04', 1, 3.5, 'Москва', true, true, 4),
       (3, 'Пальто Top Man M', 'Мужское пальто размера М, в нормальном состоянии',
        4200.00, 0, false, '2022-09-12', 2, 0, 'Санкт-Петербург', false, false, 0),
        (2, 'Винтвжный аудио проигрыватель', 'Винтажный аудио проигрыватель, 1960-ых годов выпуска, в рабочем состоянии',
        143000.00, 0, false, '2022-09-12', 1, 4, 'Москва', true, false, 0),
        (5, 'Nike Air Force 1 43 размер', 'Полностью новые Nike Air Force 1 с у паковкой, 43 размер (9.5 UK, 10.5 US)',
        9500.00, 200.00, false, '2022-11-09', 2, 3.5, 'Москва', false, true, 0),
        (5, 'Набор кухонной посуды Some Random Name', 'Набор кухонной посуды из 5 тарелок, 2 салатниц, 1 блюда и 5 стаканов',
         3250.00, 100.00, true, '2022-11-15', 3, 3.5, 'Москва', false, false, 2);

--INSERT INTO messages (sender_id, receiver_id, content, time)
--VALUES (2, 3, 'Здравствуйте, хотел бы узнать у вас о возможности снижения цены на Айфон 13',
--        '2022-09-08 14:47:13.270377'),
--       (3, 2, 'Могу рассмотреть вариант снижения цены на сумму не больше 5 тысяч рублей' ,
--        '2022-09-08 14:49:02.327292');

INSERT INTO reviews (author_id, post_id, content, time, grade)
VALUES (3, 3, 'Отличная консоль для бюджетного гейминга, всем доволен', '2022-09-17 15:13:22.270377', 4),
(4, 4, 'Наушники в нормальном состоянии, звук за свои деньги приемлимый', '2022-11-25 12:23:54.350657', 4),
(2, 8, 'На паре таредок были различного рода дефекты (Скол и трещина). Слегка разочарован', '2022-11-29 18:47:11.720451', 3);