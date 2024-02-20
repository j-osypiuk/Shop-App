---------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS category_category_id_seq
AS bigint
INCREMENT BY 1
START WITH 1;

CREATE TABLE IF NOT EXISTS category(
    category_id bigint DEFAULT nextval('category_category_id_seq') PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE,
    description text
);

-------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS discount_discount_id_seq
AS bigint
INCREMENT BY 1
START WITH 1;

CREATE TABLE IF NOT EXISTS discount(
    discount_id bigint DEFAULT nextval('discount_discount_id_seq') PRIMARY KEY,
    name varchar(100) NOT NULL,
    discount_percent integer NOT NULL,
    description text
);

-------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS product_product_id_seq
AS bigint
INCREMENT BY 1
START WITH 1;

CREATE TABLE IF NOT EXISTS product(
    product_id bigint DEFAULT nextval('product_product_id_seq') PRIMARY KEY,
    name varchar(50) NOT NULL,
    description text NOT NULL,
    amount int NOT NULL,
    price float8 NOT NULL,
    discount_id bigint,

    CONSTRAINT discount_id_fk FOREIGN KEY (discount_id) REFERENCES discount(discount_id)
);

-------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS product_category(
    product_id bigint NOT NULL,
    category_id bigint NOT NULL,

    CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES product(product_id),
    CONSTRAINT category_id_fk FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS product_photo(
    photo_name uuid PRIMARY KEY,
    number int NOT NULL,
    product_id bigint NOT NULL,

    CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS address_address_id_seq
AS bigint
INCREMENT BY 1
START WITH 1;

CREATE TABLE IF NOT EXISTS address(
    address_id bigint DEFAULT nextval('address_address_id_seq') PRIMARY KEY,
    country varchar(50) NOT NULL,
    region varchar(50) NOT NULL,
    city varchar(50) NOT NULL,
    street varchar(50) NOT NULL,
    number varchar(5) NOT NULL,
    postal_code varchar(10) NOT NULL
);

-------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS _user_user_id_seq
AS bigint
INCREMENT BY 1
START WITH 1;

CREATE TABLE IF NOT EXISTS _user(
    user_id bigint DEFAULT nextval('_user_user_id_seq') PRIMARY KEY,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    email varchar(100) NOT NULL UNIQUE,
    password varchar(100) NOT NULL,
    birth_date timestamp(6) without time zone NOT NULL,
    gender varchar(7) NOT NULL,
    phone_number varchar(10) UNIQUE,
    role varchar(14) NOT NULL,
    address_id bigint NOT NULL UNIQUE,

    CONSTRAINT _user_gender_check CHECK (gender IN ('MALE', 'FEMALE')),
    CONSTRAINT _user_role_chech CHECK (role IN ('ROLE_CUSTOMER', 'ROLE_EMPLOYEE', 'ROLE_ADMIN')),
    CONSTRAINT address_id_fk FOREIGN KEY (address_id) REFERENCES address(address_id)
);

-------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS order_details_order_id_seq
AS bigint
INCREMENT BY 1
START WITH 1;

CREATE TABLE IF NOT EXISTS order_details(
    order_id bigint DEFAULT nextval('order_details_order_id_seq') PRIMARY KEY,
    order_date timestamp(6) without time zone NOT NULL,
    total_price float8 NOT NULL,
    total_discount float8 NOT NULL,
    completed boolean NOT NULL,
    address_id bigint NOT NULL,
    user_id bigint NOT NULL,

    CONSTRAINT address_id_fk FOREIGN KEY (address_id) REFERENCES address(address_id),
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES _user(user_id)
);

-------------------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS order_product_order_product_id_seq
AS bigint
INCREMENT BY 1
START WITH 1;

CREATE TABLE IF NOT EXISTS order_product(
    order_product_id bigint DEFAULT nextval('order_product_order_product_id_seq') PRIMARY KEY,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    amount int NOT NULL,

    CONSTRAINT order_id_fk FOREIGN KEY (order_id) REFERENCES order_details(order_id),
    CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-------------------------------------------------------------------------------------

INSERT INTO category(name, description)
VALUES ('drinks', 'Drinks category description.');

INSERT INTO category(name, description)
VALUES ('sweets', 'Sweets category description.');

INSERT INTO category(name, description)
VALUES ('dairy', 'Dairy category description.');

INSERT INTO discount(name, discount_percent, description)
VALUES ('saturday discount', 15, 'Saturday discount description.');

INSERT INTO product(name, description, amount, price, discount_id)
VALUES ('water', 'Mineral water.', 150, 2.0, null);

INSERT INTO product_category(product_id, category_id)
VALUES ((SELECT product_id FROM product WHERE name = 'water'),
        (SELECT category.category_id FROM category WHERE name = 'drinks'));

INSERT INTO product(name, description, amount, price, discount_id)
VALUES ('orange juice', 'Fresh orange juice.', 100, 3.0, null);

INSERT INTO product_category(product_id, category_id)
VALUES ((SELECT product_id FROM product WHERE name = 'orange juice'),
        (SELECT category.category_id FROM category WHERE name = 'drinks'));

INSERT INTO product(name, description, amount, price, discount_id)
VALUES ('peppermint candies', 'Super fresh peppermint candies.', 400, 0.1, 1);

INSERT INTO product_category(product_id, category_id)
VALUES ((SELECT product_id FROM product WHERE name = 'peppermint candies'),
        (SELECT category.category_id FROM category WHERE name = 'sweets'));

INSERT INTO product(name, description, amount, price, discount_id)
VALUES ('jelly candy', 'Sour jelly candy.', 400, 0.2, 1);

INSERT INTO product_category(product_id, category_id)
VALUES ((SELECT product_id FROM product WHERE name = 'jelly candy'),
        (SELECT category.category_id FROM category WHERE name = 'sweets'));

INSERT INTO product(name, description, amount, price, discount_id)
VALUES ('roll', 'Wheat roll.', 200, 0.1, null);

INSERT INTO product_category(product_id, category_id)
VALUES ((SELECT product_id FROM product WHERE name = 'roll'),
        (SELECT category.category_id FROM category WHERE name = 'dairy'));

INSERT INTO product(name, description, amount, price, discount_id)
VALUES ('bread', 'Wheat bread.', 100, 1.2, null);

INSERT INTO product_category(product_id, category_id)
VALUES ((SELECT product_id FROM product WHERE name = 'bread'),
        (SELECT category.category_id FROM category WHERE name = 'dairy'));


INSERT INTO address(country, region, city, street, number, postal_code)
VALUES ('Poland', 'Mazovia', 'Warsaw', 'Dluga', '23', '11-111');

INSERT INTO address(country, region, city, street, number, postal_code)
VALUES ('Poland', 'Lublin_region', 'Lublin', 'Krotka', '42', '22-222');

INSERT INTO address(country, region, city, street, number, postal_code)
VALUES ('Poland', 'Pomerania', 'Sopot', 'Srednia', '54', '33-333');

INSERT INTO _user(first_name, last_name, email, password, birth_date, gender, phone_number, role, address_id)
VALUES ('David',
        'Crump',
        'adm@mail.com',
        '$2a$10$b/FRPzv2v8PrEH/BiwJUk.lYFRtU9VTCj79wRB/y6YKOmk.AOviD.', -- password='password'
        '2000-08-27 00:00:00.000000',
        'MALE',
        '999888777',
        'ROLE_ADMIN',
        1);

INSERT INTO _user(first_name, last_name, email, password, birth_date, gender, phone_number, role, address_id)
VALUES ('Pablo',
        'Picolo',
        'emp@mail.com',
        '$2a$10$b/FRPzv2v8PrEH/BiwJUk.lYFRtU9VTCj79wRB/y6YKOmk.AOviD.', -- password='password'
        '1998-08-27 00:00:00.000000',
        'MALE',
        '111222333',
        'ROLE_EMPLOYEE',
        2);

INSERT INTO _user(first_name, last_name, email, password, birth_date, gender, phone_number, role, address_id)
VALUES ('Eva',
        'Pulmer',
        'cus@mail.com',
        '$2a$10$b/FRPzv2v8PrEH/BiwJUk.lYFRtU9VTCj79wRB/y6YKOmk.AOviD.', -- password='password'
        '2001-08-27 00:00:00.000000',
        'FEMALE',
        '444555666',
        'ROLE_CUSTOMER',
        3);