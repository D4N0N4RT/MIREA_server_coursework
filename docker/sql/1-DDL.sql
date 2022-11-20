\c courseworkDB;

CREATE SEQUENCE IF NOT EXISTS users_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE users_id_seq OWNER TO db_user;

CREATE SEQUENCE IF NOT EXISTS posts_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE posts_id_seq OWNER TO db_user;

CREATE SEQUENCE IF NOT EXISTS messages_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE messages_id_seq OWNER TO db_user;

CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    "email" VARCHAR(50) NOT NULL,
    "password" VARCHAR(255),
    "name" VARCHAR(50),
    surname VARCHAR(75),
    phone VARCHAR(10),
    city VARCHAR(50),
    registration_date DATE,
    rating INTEGER,
    "role" VARCHAR(25),
    activity BOOLEAN
);

ALTER TABLE users OWNER TO db_user;

ALTER SEQUENCE users_id_seq OWNED BY users.id;

CREATE TABLE IF NOT EXISTS posts
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    user_id BIGSERIAL,
    title VARCHAR(100),
    description TEXT,
    price DECIMAL(12,2),
    promotion DECIMAL(12,2),
    sold BOOLEAN,
    posting_date DATE,
    "category" VARCHAR(50),
    seller_rating INTEGER,
    city VARCHAR(50),
    exchanged BOOLEAN,
    buyer_id BIGSERIAL,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE posts OWNER TO db_user;

ALTER SEQUENCE posts_id_seq OWNED BY posts.id;

CREATE TABLE IF NOT EXISTS messages
(
    id BIGSERIAL NOT NULL PRIMARY KEY ,
    sender_id BIGSERIAL,
    receiver_id BIGSERIAL,
    "content" TEXT,
    "time" TIMESTAMP,
    FOREIGN KEY(sender_id) REFERENCES users(id),
    FOREIGN KEY(receiver_id) REFERENCES users(id)
);

ALTER TABLE messages OWNER TO db_user;

ALTER SEQUENCE messages_id_seq OWNED BY messages.id;