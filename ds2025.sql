BEGIN;

-- ============================
-- TABLA: tperson
-- ============================

CREATE TABLE tperson (
    idperson CHAR(36) PRIMARY KEY,
    firstname VARCHAR(70) NOT NULL,
    surname VARCHAR(40) NOT NULL,
    dni CHAR(8) NOT NULL,
    gender BOOLEAN NOT NULL,
    birthdate TIMESTAMP NOT NULL,
    createdat TIMESTAMP NOT NULL,
    updatedat DATE NOT NULL
);

INSERT INTO tperson (idperson, firstname, surname, dni, gender, birthdate, createdat, updatedat) VALUES
('00d080d2-b0ba-4f16-8e8d-4f0293e60ea2', 'carlos', 'olivera', '12345678', true, '2004-12-09 00:00:00', '2026-01-08 15:41:54', '2026-01-08'),
('40b7f566-15c2-4e11-ac4d-ff384c36c2c2', 'Users', 'User', '09876543', false, '2000-09-13 00:00:00', '2025-12-12 22:19:19', '2025-12-12'),
('430f5dd8-2820-47d0-8ec8-6dc2f15d0679', 'client', 'admin', '01234567', true, '2004-10-13 00:00:00', '2025-12-11 19:17:31', '2025-12-11'),
('578ce75d-0058-4cd7-918f-27437fdbaf8a', 'Client-1', 'Exanple', '12345678', true, '1992-12-31 00:00:00', '2025-12-12 16:56:20', '2025-12-12'),
('73f2ca8e-8a7e-4944-85b4-977833186280', 'Juan', 'Aroni', '12345678', true, '1992-12-31 00:00:00', '2025-12-11 03:30:13', '2025-12-11'),
('7bda786f-1781-4127-ae46-173f745c0dfa', 'Juan', 'Aroni', '12345678', true, '1992-12-31 00:00:00', '2025-12-11 15:30:32', '2025-12-11'),
('9f5f44c8-b49b-44f9-9e6a-8daf9a099387', 'client', 'admin', '01234567', true, '2004-10-13 00:00:00', '2025-12-11 19:17:51', '2025-12-11'),
('c28e4f9d-5235-48cd-a024-7e8e163c0fd4', 'Juan', 'Aroni', '12345678', true, '1992-12-31 00:00:00', '2025-12-12 16:36:45', '2025-12-12'),
('c9317a35-2a37-47bb-be42-3c90226d8782', 'Juan', 'Mark', '36985214', true, '2000-12-09 00:00:00', '2026-01-08 15:57:27', '2026-01-08'),
('eabefb23-0250-4f43-9c3d-debd5a77f6a2', 'Client-1', 'Exanple', '12345678', true, '1992-12-31 00:00:00', '2025-12-12 16:51:39', '2025-12-12');

-- ============================
-- TABLA: users
-- ============================

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

INSERT INTO users (id, username, password) VALUES
(1, 'carlos', '123456');

SELECT setval(pg_get_serial_sequence('users','id'), 1, true);

COMMIT;
