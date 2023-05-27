CREATE TABLE categories (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL
);

SELECT * FROM categories;

CREATE TABLE products (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    price BIGINT NOT NULL,
    categories_id BIGINT NOT NULL,
    CONSTRAINT fk_products_categories FOREIGN KEY (categories_id) REFERENCES categories(id)
);

SELECT * FROM products;

ALTER TABLE categories ADD COLUMN created_date TIMESTAMP;
ALTER TABLE categories ADD COLUMN updated_date TIMESTAMP;
ALTER TABLE categories RENAME updated_date TO last_modified_date;