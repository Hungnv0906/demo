-- Smart Home Database Schema
-- Version: 1.0
-- Description: Complete schema for Smart Home application

-- Create users table
CREATE TABLE users (
    user_id     INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rfid_tag    VARCHAR(50)  UNIQUE,
    full_name   VARCHAR(100) NOT NULL,
    user_role   VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_rfid_tag (rfid_tag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create devices table
CREATE TABLE devices (
    device_id      INT AUTO_INCREMENT PRIMARY KEY,
    device_name    VARCHAR(100) NOT NULL,
    device_type    VARCHAR(50)  NOT NULL,
    mac_address    VARCHAR(20)  UNIQUE,
    mqtt_topic     VARCHAR(100) UNIQUE,
    current_status VARCHAR(50)  NOT NULL DEFAULT 'OFF',
    last_updated   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_device_type (device_type),
    INDEX idx_mqtt_topic  (mqtt_topic)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create access_logs table
CREATE TABLE access_logs (
    access_id     INT AUTO_INCREMENT PRIMARY KEY,
    rfid_tag      VARCHAR(50)  NOT NULL,
    user_id       INT,
    access_status VARCHAR(20)  NOT NULL,
    accessed_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_access_user_id   (user_id),
    INDEX idx_access_accessed_at (accessed_at),
    INDEX idx_access_rfid      (rfid_tag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create environment_logs table
CREATE TABLE environment_logs (
    log_id      INT AUTO_INCREMENT PRIMARY KEY,
    temperature FLOAT        NOT NULL,
    humidity    FLOAT        NOT NULL,
    recorded_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_env_recorded_at (recorded_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS alert_logs (
    alert_id    INT AUTO_INCREMENT PRIMARY KEY,
    alert_type  VARCHAR(50) NOT NULL, 
    message     TEXT NOT NULL,         
    threshold   FLOAT,                 
    actual_value FLOAT,                
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample data: users (password = "password123", BCrypt)
INSERT INTO users (username, password_hash, rfid_tag, full_name, user_role) VALUES
('admin',  '$2b$12$N4paqgMdUGICLQGad6ZO3uYgEF.7yuYf/hjAgy5xKyAVBTUbPJQtO', 'RFID-ADMIN-001', 'Administrator', 'ADMIN'),
('user1',  '$2b$12$GIRN64JwBVJhuhXadTPJ0ufJI3wMqySkk4CFyI9iTWfHAlw.rLGom', 'RFID-USER-001',  'Nguyen Van A',  'USER'),
('user2',  '$2b$12$WMuYJqlbT9S4G6zAIcsaae5TTgJ2Yzkm0oRT/oq/FjieH2KyzX19y', 'RFID-USER-002',  'Tran Thi B',    'USER');

-- Sample data: devices
INSERT INTO devices (device_name, device_type, mac_address, mqtt_topic, current_status) VALUES
('Living Room Light',  'LIGHT',  'AA:BB:CC:DD:EE:01', 'home/living/light',   'OFF'),
('Bedroom Fan',        'FAN',    'AA:BB:CC:DD:EE:02', 'home/bedroom/fan',    'OFF'),
('Air Conditioner',    'AC',     'AA:BB:CC:DD:EE:03', 'home/ac',             'OFF'),
('Front Door Lock',    'LOCK',   'AA:BB:CC:DD:EE:04', 'home/door/lock',      'LOCKED'),
('Security Camera',    'CAMERA', 'AA:BB:CC:DD:EE:05', 'home/camera/front',   'ON');

-- Sample data: access_logs
INSERT INTO access_logs (rfid_tag, user_id, access_status) VALUES
('RFID-ADMIN-001', 1, 'SUCCESS'),
('RFID-USER-001',  2, 'SUCCESS'),
('RFID-UNKNOWN',   NULL, 'DENIED');

-- Sample data: environment_logs
INSERT INTO environment_logs (temperature, humidity) VALUES
(25.5, 65.0),
(26.0, 63.5),
(27.1, 60.2),
(24.8, 68.0),
(25.0, 66.5);
