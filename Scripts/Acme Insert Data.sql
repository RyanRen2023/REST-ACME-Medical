-- ACMEMedical data.sql file

USE `databank`;

-- Insert data into medical school
INSERT INTO medical_school (name, is_public, created, updated, version) VALUES
('Harvard Medical School', TRUE, NOW(), NOW(), 1),
('Stanford Medical School', TRUE, NOW(), NOW(), 1),
('Private Medical Institute', FALSE, NOW(), NOW(), 1);

-- Insert data into medical training
INSERT INTO medical_training (school_id, start_date, end_date, active, created, updated, version) VALUES
(1, '2022-01-01', '2023-01-01', TRUE, NOW(), NOW(), 1),
(2, '2023-02-01', '2024-02-01', TRUE, NOW(), NOW(), 1);

-- Insert data into physician
INSERT INTO physician (first_name, last_name, created, updated, version) VALUES
('John', 'Doe', NOW(), NOW(), 1),
('Jane', 'Smith', NOW(), NOW(), 1);

-- Insert data into patient
INSERT INTO patient (first_name, last_name, year_of_birth, home_address, height_cm, weight_kg, smoker, created, updated, version) VALUES
('Alice', 'Johnson', 1980, '123 Main St', 165, 70, FALSE, NOW(), NOW(), 1),
('Bob', 'Brown', 1975, '456 Elm St', 180, 85, TRUE, NOW(), NOW(), 1);

-- Insert data into medicine
INSERT INTO medicine (drug_name, manufacturer_name, dosage_information, chemical_name, generic_name, created, updated, version) VALUES
('Aspirin', 'PharmaCorp', '100mg', 'Acetylsalicylic Acid', 'ASA', NOW(), NOW(), 1),
('Paracetamol', 'HealthCorp', '500mg', 'Acetaminophen', 'Tylenol', NOW(), NOW(), 1);

-- Insert data into prescription
INSERT INTO prescription (physician_id, patient_id, medicine_id, number_of_refills, prescription_information, created, updated, version) VALUES
(1, 1, 1, 2, 'Take one tablet daily', NOW(), NOW(), 1),
(2, 2, 2, 3, 'Take two tablets after meals', NOW(), NOW(), 1);

-- Insert data into medical_certificate
INSERT INTO medical_certificate (physician_id, training_id, signed, created, updated, version) VALUES
(1, 1, TRUE, NOW(), NOW(), 1),
(2, 2, FALSE, NOW(), NOW(), 1);

-- Insert data into security_role
INSERT INTO security_role (name) VALUES
('ADMIN_ROLE'),
('USER_ROLE');

-- Insert data into security_user
INSERT INTO security_user (username, password_hash, physician_id) VALUES
('admin', 'hashed_password_here', 1),
('user1', 'hashed_password_here', 2);

-- Insert data into user_has_role
INSERT INTO user_has_role (user_id, role_id) VALUES
(1, 1),
(2, 2);
