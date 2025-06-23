-- Create the database 'pismo_banking' if it doesn't already exist
-- and switch the context to use it for subsequent operations.
CREATE DATABASE IF NOT EXISTS pismo_banking;
USE pismo_banking;

-- Drop tables in dependency-safe order
DROP TABLE IF EXISTS transactions_seq;
DROP TABLE IF EXISTS accounts_seq;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;

-- Accounts table
CREATE TABLE IF NOT EXISTS accounts (
  account_id BIGINT NOT NULL,
  document_number VARCHAR(255) NOT NULL,
  PRIMARY KEY (account_id)
);

-- Simulated sequence table for accounts
CREATE TABLE IF NOT EXISTS accounts_seq (
  next_val BIGINT DEFAULT NULL
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
  transaction_id BIGINT NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  operations_type_id INT NOT NULL,
  account_id BIGINT NOT NULL,
  event_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (transaction_id),
  KEY account_id_idx (account_id),
  CONSTRAINT fk_transactions_account_id FOREIGN KEY (account_id) REFERENCES accounts (account_id)
);

-- Simulated sequence table for transactions
CREATE TABLE IF NOT EXISTS transactions_seq (
  next_val BIGINT DEFAULT NULL
);