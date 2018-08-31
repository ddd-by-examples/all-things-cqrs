CREATE TABLE IF NOT EXISTS credit_card (
  id            UUID PRIMARY KEY,
  initial_limit DECIMAL(18,2) NOT NULL,
  used_limit    DECIMAL(18,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS stored_domain_event (
  id     UUID PRIMARY KEY,
  content   VARCHAR2(4096)    NOT NULL,
  sent   BOOLEAN    NOT NULL,
  event_timestamp   DATETIME   NOT NULL,
  event_type VARCHAR2(128) NOT NULL
);