CREATE SEQUENCE expenses_id_seq
INCREMENT BY 1
NO MAXVALUE
NO MINVALUE
CACHE 1;

CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT DEFAULT nextval('expenses_id_seq') PRIMARY KEY,
    amount NUMERIC NOT NULL,
    total_balance NUMERIC,
    description VARCHAR(255) NOT NULL,
    operation_date TIMESTAMP NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    source VARCHAR(255) ,
    fk_category BIGINT REFERENCES categories (id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
