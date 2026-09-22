CREATE DATABASE IF NOT EXISTS gestion_productos;
USE gestion_productos;

CREATE TABLE IF NOT EXISTS productos (
     id INT AUTO_INCREMENT PRIMARY KEY,
     codigo VARCHAR(50) NOT NULL UNIQUE,
     nombre VARCHAR(100) NOT NULL,
     categoria VARCHAR(50),
     precio DECIMAL(10, 2) NOT NULL,
     stock INT NOT NULL,
     estado VARCHAR(20) DEFAULT 'Activo',
     CONSTRAINT chk_precio CHECK (precio > 0),
     CONSTRAINT chk_stock CHECK (stock >= 0)
);

-- Datos de prueba (opcional)
INSERT IGNORE INTO productos (codigo, nombre, categoria, precio, stock, estado) VALUES
('P001', 'Laptop Lenovo', 'Electrónica', 2500.00, 15, 'Activo'),
('P002', 'Mouse Logitech', 'Electrónica', 45.50, 3, 'Activo'),
('P003', 'Teclado Mecánico', 'Electrónica', 120.00, 8, 'Activo'),
('P004', 'Silla Oficina', 'Muebles', 350.00, 2, 'Activo');