-- ACMEMedical create.sql file

-- Drop existing databank schema if it exists
DROP SCHEMA IF EXISTS `databank`;

-- Use or create the databank schema
CREATE SCHEMA IF NOT EXISTS `databank` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `databank`;

-- Create table for medical school
CREATE TABLE medical_school (
                                school_id INT PRIMARY KEY AUTO_INCREMENT,
                                name VARCHAR(100) NOT NULL,
                                is_public BOOLEAN NOT NULL,
                                created DATETIME NOT NULL,
                                updated DATETIME NOT NULL,
                                version BIGINT NOT NULL
);

-- Create table for medical training
CREATE TABLE medical_training (
                                  training_id INT PRIMARY KEY AUTO_INCREMENT,
                                  school_id INT NOT NULL,
                                  start_date DATETIME NOT NULL,
                                  end_date DATETIME NOT NULL,
                                  active BOOLEAN NOT NULL,
                                  created DATETIME NOT NULL,
                                  updated DATETIME NOT NULL,
                                  version BIGINT NOT NULL,
                                  FOREIGN KEY (school_id) REFERENCES medical_school(school_id)
);

-- Create table for physician
CREATE TABLE physician (
                           physician_id INT PRIMARY KEY AUTO_INCREMENT,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           created DATETIME NOT NULL,
                           updated DATETIME NOT NULL,
                           version BIGINT NOT NULL
);

-- Create table for patient
CREATE TABLE patient (
                         patient_id INT PRIMARY KEY AUTO_INCREMENT,
                         first_name VARCHAR(50) NOT NULL,
                         last_name VARCHAR(50) NOT NULL,
                         year_of_birth INT NOT NULL,
                         home_address VARCHAR(100),
                         height_cm INT,
                         weight_kg INT,
                         smoker BOOLEAN,
                         created DATETIME NOT NULL,
                         updated DATETIME NOT NULL,
                         version BIGINT NOT NULL
);

-- Create table for medicine
CREATE TABLE medicine (
                          medicine_id INT PRIMARY KEY AUTO_INCREMENT,
                          drug_name VARCHAR(50) NOT NULL,
                          manufacturer_name VARCHAR(50) NOT NULL,
                          dosage_information VARCHAR(100),
                          chemical_name VARCHAR(50),
                          generic_name VARCHAR(50),
                          created DATETIME NOT NULL,
                          updated DATETIME NOT NULL,
                          version BIGINT NOT NULL
);

-- Create table for prescription
CREATE TABLE prescription (
                              prescription_id INT PRIMARY KEY AUTO_INCREMENT,
                              physician_id INT NOT NULL,
                              patient_id INT NOT NULL,
                              medicine_id INT NOT NULL,
                              number_of_refills INT,
                              prescription_information VARCHAR(100),
                              created DATETIME NOT NULL,
                              updated DATETIME NOT NULL,
                              version BIGINT NOT NULL,
                              FOREIGN KEY (physician_id) REFERENCES physician(physician_id),
                              FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
                              FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id)
);

-- Create table for medical_certificate
CREATE TABLE medical_certificate (
                                     certificate_id INT PRIMARY KEY AUTO_INCREMENT,
                                     physician_id INT NOT NULL,
                                     training_id INT NOT NULL,
                                     signed BOOLEAN NOT NULL,
                                     created DATETIME NOT NULL,
                                     updated DATETIME NOT NULL,
                                     version BIGINT NOT NULL,
                                     FOREIGN KEY (physician_id) REFERENCES physician(physician_id),
                                     FOREIGN KEY (training_id) REFERENCES medical_training(training_id)
);

-- Create table for security_role
CREATE TABLE security_role (
                               role_id INT PRIMARY KEY AUTO_INCREMENT,
                               name VARCHAR(45) NOT NULL
);

-- Create table for security_user
CREATE TABLE security_user (
                               user_id INT PRIMARY KEY AUTO_INCREMENT,
                               username VARCHAR(100) NOT NULL,
                               password_hash VARCHAR(256) NOT NULL,
                               physician_id INT,
                               FOREIGN KEY (physician_id) REFERENCES physician(physician_id)
);

-- Create table for user_has_role
CREATE TABLE user_has_role (
                               user_id INT NOT NULL,
                               role_id INT NOT NULL,
                               PRIMARY KEY (user_id, role_id),
                               FOREIGN KEY (user_id) REFERENCES security_user(user_id),
                               FOREIGN KEY (role_id) REFERENCES security_role(role_id)
);