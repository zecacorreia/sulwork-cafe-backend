CREATE TABLE IF NOT EXISTS collaborator (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  cpf  VARCHAR(11)  NOT NULL,
  created_at TIMESTAMPTZ DEFAULT now(),
  CONSTRAINT uq_collaborator_cpf UNIQUE (cpf)
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_collaborator_name_lower ON collaborator ((lower(name)));

CREATE TABLE IF NOT EXISTS coffee_event (
  id BIGSERIAL PRIMARY KEY,
  event_date DATE NOT NULL,
  created_at TIMESTAMPTZ DEFAULT now(),
  CONSTRAINT uq_event_date UNIQUE (event_date)
);

CREATE TABLE IF NOT EXISTS coffee_item (
  id BIGSERIAL PRIMARY KEY,
  event_id BIGINT NOT NULL REFERENCES coffee_event(id) ON DELETE CASCADE,
  collaborator_id BIGINT NOT NULL REFERENCES collaborator(id) ON DELETE RESTRICT,
  item_name VARCHAR(120) NOT NULL,
  brought BOOLEAN NULL,
  checked_at TIMESTAMPTZ NULL,
  created_at TIMESTAMPTZ DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_item_per_day ON coffee_item (event_id, lower(item_name));

ALTER TABLE collaborator
  ADD CONSTRAINT IF NOT EXISTS ck_cpf_digits CHECK (cpf ~ '^[0-9]{11}$');
