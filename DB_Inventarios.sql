CREATE DATABASE pruebajava;
USE pruebajava;

/* Crear la tabla Roles*/
CREATE TABLE Roles (
    idRol INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(20) UNIQUE NOT NULL
);

/* Crear la tabla Usuarios*/
CREATE TABLE Usuarios (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL,
    Email VARCHAR(50) UNIQUE NOT NULL,
    Contraseña VARCHAR(100) NOT NULL,
    idRol INT NOT NULL,
    FOREIGN KEY (idRol) REFERENCES Roles(idRol)
);

/*Crear la tabla Productos*/
CREATE TABLE Productos (
    idProducto INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(40) NOT NULL,
    Descripcion TEXT,
    Precio DECIMAL(16,2) NOT NULL,
    Estatus BOOLEAN NOT NULL DEFAULT TRUE
);

/*Crear la tabla Inventario*/
CREATE TABLE Inventario (
    idInventario INT PRIMARY KEY AUTO_INCREMENT,
    idProducto INT NOT NULL,
    Cantidad INT NOT NULL DEFAULT 0,
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

/*Crear la tabla Movimientos*/
CREATE TABLE Movimientos (
    idMovimiento INT PRIMARY KEY AUTO_INCREMENT,
    idInventario INT NOT NULL,
    idUsuario INT NOT NULL,
    TipoMovimiento ENUM('Entrada', 'Salida') NOT NULL,
    Cantidad INT NOT NULL,
    FechaHora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idInventario) REFERENCES Inventario(idInventario),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);

-- Insertar roles
INSERT INTO Roles (Nombre) VALUES ('Administrador');
INSERT INTO Roles (Nombre) VALUES ('Almacenista');

-- Insertar usuario de prueba
INSERT INTO Usuarios (Nombre, Email, Contraseña, idRol) VALUES ('Admin', 'admin@example.com', 'admin123', 1);
INSERT INTO Usuarios (Nombre, Email, Contraseña, idRol) VALUES ('Juan', 'juan@example.com', 'juan123', 2);