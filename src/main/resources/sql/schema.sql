DROP TABLE IF EXISTS transaction_seq;
DROP TABLE IF EXISTS account_seq;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS account;

CREATE TABLE IF NOT EXISTS account(

  account_id bigint NOT NULL,
  document_number varchar(255) NOT NULL,
  event_date datetime(6) DEFAULT NULL,
  PRIMARY KEY (account_id)
);

CREATE TABLE IF NOT EXISTS account_seq (
  next_val bigint DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
  amount decimal(42,2) NOT NULL,
  operations_type_id int NOT NULL,
  account_id bigint NOT NULL,
  event_date datetime(6) DEFAULT NULL,
  transaction_id bigint NOT NULL,
  PRIMARY KEY (transaction_id),
  KEY account_id_idx (account_id),
  CONSTRAINT account_id FOREIGN KEY (account_id) REFERENCES account (account_id)
);

CREATE TABLE IF NOT EXISTS transaction_seq (
  next_val bigint DEFAULT NULL
);



