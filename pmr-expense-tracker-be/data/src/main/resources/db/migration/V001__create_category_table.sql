CREATE SEQUENCE categories_id_seq
INCREMENT BY 1
NO MAXVALUE
NO MINVALUE
CACHE 1;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT DEFAULT nextval('categories_id_seq') PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    fk_parent_category BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
);