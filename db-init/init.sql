CREATE DATABASE IF NOT EXISTS pipdb;
USE pipdb;

CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  phone_number VARCHAR(15) NOT NULL,
  email_id VARCHAR(100) NOT NULL UNIQUE,
  role ENUM('ADMIN', 'USER') NOT NULL
);

INSERT INTO users (user_name, password, phone_number, email_id, role)
VALUES
  ('AdminUser', 'user123', '+911234567890', 'admin@pip.com', 'ADMIN'),
  ('User', 'user123', '+919876543210', 'user@pip.com', 'USER')
ON DUPLICATE KEY UPDATE
  user_name = VALUES(user_name),
  password = VALUES(password),
  phone_number = VALUES(phone_number),
  role = VALUES(role);
