-- Drop table if it exists
DROP TABLE IF EXISTS devices;

-- Create the devices table
CREATE TABLE devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pin_code BIGINT NOT NULL UNIQUE CHECK (pin_code >= 1000000 AND pin_code <= 9999999),
    status VARCHAR(20) NOT NULL,
    temperature DOUBLE NOT NULL
);

-- Insert dummy data
INSERT INTO devices (pin_code, status, temperature) VALUES (1234567, 'READY', -1.0);
INSERT INTO devices (pin_code, status, temperature) VALUES (2345678, 'ACTIVE', 4.5);
INSERT INTO devices (pin_code, status, temperature) VALUES (3456789, 'ACTIVE', 9.0);
INSERT INTO devices (pin_code, status, temperature) VALUES (4567890, 'READY', -1.0);
INSERT INTO devices (pin_code, status, temperature) VALUES (5678901, 'ERROR', 15.0);
INSERT INTO devices (pin_code, status, temperature) VALUES (6789012, 'INACTIVE', 25.0);
INSERT INTO devices (pin_code, status, temperature) VALUES (7890123, 'ACTIVE', 6.3);
INSERT INTO devices (pin_code, status, temperature) VALUES (8901234, 'READY', -1.0);
INSERT INTO devices (pin_code, status, temperature) VALUES (9012345, 'ACTIVE', 1.5);
INSERT INTO devices (pin_code, status, temperature) VALUES (1123456, 'ACTIVE', 8.7);