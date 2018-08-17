CREATE TABLE IF NOT EXISTS credit_card (
  id            CHAR(36),
  initial_limit DECIMAL(18,2) NOT NULL,
  used_limit    DECIMAL(18,2) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS withdrawal (
  id     CHAR(36) PRIMARY KEY,
  card_id   CHAR(36)    NOT NULL,
  amount DECIMAL(18,2) NOT NULL
);

