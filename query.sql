CREATE DATABASE IF NOT EXISTS gestion_productos;
USE gestion_productos;

CREATE TABLE IF NOT EXISTS productos (
     id INT AUTO_INCREMENT PRIMARY KEY,
     codigo VARCHAR(50) NOT NULL UNIQUE,
     nombre VARCHAR(100) NOT NULL,
     categoria VARCHAR(50),
     precio INT NOT NULL,
     stock INT NOT NULL,
     estado VARCHAR(20) DEFAULT 'Activo',
     CONSTRAINT chk_precio CHECK (precio > 0),
     CONSTRAINT chk_stock CHECK (stock >= 0)
);

-- Datos de prueba (Gs, sin decimales)
INSERT IGNORE INTO productos (codigo, nombre, categoria, precio, stock, estado) VALUES
('P001', 'Laptop Lenovo', 'Electronica', 7500000, 15, 'Activo'),
('P002', 'Mouse Logitech', 'Electronica', 150000, 3, 'Activo'),
('P003', 'Teclado Mecánico', 'Electronica', 450000, 8, 'Activo'),
('P004', 'Silla Oficina', 'Muebles', 1200000, 2, 'Activo');