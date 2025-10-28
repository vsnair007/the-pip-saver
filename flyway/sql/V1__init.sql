-- init.sql for PostgreSQL (re-runnable)

-- Create table if not exists
CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL PRIMARY KEY,
  user_name VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  phone_number VARCHAR(15) NOT NULL,
  email_id VARCHAR(100) NOT NULL UNIQUE,
  role VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN', 'USER'))
);

-- Insert initial users with fixed IDs and avoid duplicates
INSERT INTO users (user_id, user_name, password, phone_number, email_id, role)
VALUES
  (1, 'AdminUser', '$2a$10$xLmU7ntn7rua0eLVx7ip6.OGKu3PiRsaFOEkraLqVoEHKw4Du2/ay', '+911234567890', 'admin@pip.com', 'ADMIN'),
  (2, 'User', '$2a$10$xLmU7ntn7rua0eLVx7ip6.OGKu3PiRsaFOEkraLqVoEHKw4Du2/ay', '+919876543210', 'user@pip.com', 'USER')
ON CONFLICT (email_id) DO UPDATE
SET
  user_name = EXCLUDED.user_name,
  password = EXCLUDED.password,
  phone_number = EXCLUDED.phone_number,
  role = EXCLUDED.role;

-- Adjust sequence so it doesn't conflict with existing user_ids
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_class WHERE relname = 'users_user_id_seq') THEN
    PERFORM setval('users_user_id_seq', (SELECT MAX(user_id) FROM users));
  END IF;
END
$$;