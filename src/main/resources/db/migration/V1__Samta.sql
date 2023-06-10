CREATE TABLE vendor (
  id INT NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NULL,
   `role` VARCHAR(255) NULL,
   created_at datetime NULL,
   CONSTRAINT pk_vendor PRIMARY KEY (id)
);

CREATE TABLE store_products (
  store_id INT NOT NULL,
   products_id INT NOT NULL
);

CREATE TABLE store (
  id INT NOT NULL,
   name VARCHAR(255) NULL,
   location VARCHAR(255) NULL,
   contact_detail VARCHAR(255) NULL,
   CONSTRAINT pk_store PRIMARY KEY (id)
);
CREATE TABLE product (
  id INT NOT NULL,
   name VARCHAR(255) NULL,
   type VARCHAR(255) NULL,
   manufacturer VARCHAR(255) NULL,
   price DOUBLE NOT NULL,
   units DOUBLE NOT NULL,
   CONSTRAINT pk_product PRIMARY KEY (id)
);
CREATE TABLE customer (
  id INT NOT NULL,
   first_name VARCHAR(255) NULL,
   last_name VARCHAR(255) NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NULL,
   CONSTRAINT pk_customer PRIMARY KEY (id)
);

ALTER TABLE customer ADD CONSTRAINT uc_customer_email UNIQUE (email);

ALTER TABLE store_products ADD CONSTRAINT fk_stopro_on_product FOREIGN KEY (products_id) REFERENCES product (id);

ALTER TABLE store_products ADD CONSTRAINT fk_stopro_on_store FOREIGN KEY (store_id) REFERENCES store (id);


ALTER TABLE vendor ADD CONSTRAINT uc_vendor_email UNIQUE (email);