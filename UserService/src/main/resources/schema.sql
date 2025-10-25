DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    phone_number VARCHAR(20),
    email_id VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL
);

-- Insert sample users
INSERT INTO users (user_name, password, phone_number, email_id, role) VALUES
('AdminUser', 'user123', '+911234567890', 'admin@pip.com', 'ADMIN'),
('User', 'user123', '+919876543210', 'user@pip.com', 'USER');
