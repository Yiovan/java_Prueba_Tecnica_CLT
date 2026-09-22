CREATE DATABASE IF NOT EXISTS gestion_productos;
USE gestion_productos;

CREATE TABLE productos (
     id INT AUTO_INCREMENT PRIMARY KEY,
     codigo VARCHAR(50) NOT NULL UNIQUE,
     nombre VARCHAR(100) NOT NULL,
     categoria VARCHAR(50),
     precio DECIMAL(10, 2) NOT NULL,
     stock INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'Activo'
);