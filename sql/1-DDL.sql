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

CREATE SEQUENCE IF NOT EXISTS reviews_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE reviews_id_seq OWNER TO db_user;

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR (50)
);

ALTER TABLE categories OWNER TO db_user;

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
    rating FLOAT,
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
    promotion DECIMAL(12,2) DEFAULT 0,
    sold BOOLEAN,
    posting_date DATE,
    category_id BIGINT,
    seller_rating FLOAT,
    city VARCHAR(50),
    exchanged BOOLEAN DEFAULT false,
    delivered BOOLEAN DEFAULT false,
    buyer_id BIGINT DEFAULT 0,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(category_id) REFERENCES categories(id)
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

CREATE TABLE IF NOT EXISTS reviews
(
    id BIGSERIAL NOT NULL PRIMARY KEY ,
    author_id BIGSERIAL,
    post_id BIGSERIAL,
    "content" TEXT,
    "time" TIMESTAMP,
    grade INTEGER,
    FOREIGN KEY(author_id) REFERENCES users(id),
    FOREIGN KEY(post_id) REFERENCES posts(id)
);

ALTER TABLE reviews OWNER TO db_user;

ALTER SEQUENCE reviews_id_seq OWNED BY reviews.id;

CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGSERIAL,
    token VARCHAR(255),
    expiry_date TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id)
);

ALTER TABLE refresh_tokens OWNER TO db_user;