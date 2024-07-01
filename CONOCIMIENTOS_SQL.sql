/*Creacion de la tabla productos*/
CREATE TABLE Productos (
idProductos INT(6) AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(40) NOT NULL,
precio DECIMAL(16,2) NOT NULL
);
/*Inserción de datos en la tabla Productos*/
INSERT INTO Productos (idProductos, nombre, precio) VALUES
(1, 'LAPTOP', 3000.00),
(2, 'PC', 4000.00),
(3, 'MOUSE', 100.00),
(4, 'TECLADO', 150.00),
(5, 'MONITOR', 2000.00),
(6, 'MICROFONO', 350.00),
(7, 'AUDIFONOS', 450.00);

/*Creación de la tabla Ventas*/
CREATE TABLE Ventas (
    idVentas INT(6) AUTO_INCREMENT PRIMARY KEY,
    IdProductos INT(6) NOT NULL,
    Cantidad INT(6) NOT NULL,
    FOREIGN KEY (IdProductos) REFERENCES Productos(idProductos)
);
/*Inserción de datos en la tabla Ventas*/
INSERT INTO Ventas (idVentas, IdProductos, Cantidad) VALUES
(1, 5, 8),
(2, 1, 15),
(3, 6, 13),
(4, 6, 4),
(5, 2, 3),
(6, 5, 1),
(7, 4, 5),
(8, 2, 5),
(9, 6, 2),
(10, 1, 8);

/*1.5) Traer todos los productos que tengan una venta*/
SELECT idProductos, Nombre, Precio
FROM Productos
WHERE idProductos IN (
    SELECT IdProductos
    FROM Ventas
    WHERE Cantidad = 1
);

/* 1.6) Traer todos los productos que tienen ventas y la cantidad total de productos vendidos*/
SELECT p.idProductos, p.Nombre, SUM(v.Cantidad) AS TotalVendidos
FROM Productos p
JOIN Ventas v ON p.idProductos = v.IdProductos
GROUP BY p.idProductos, p.Nombre;

/*1.7) Traer todos los productos (independientemente de si tienen ventas o no) y la suma total ($) vendida por
producto*/
SELECT p.idProductos, p.Nombre, p.Precio, 
       COALESCE(SUM(v.Cantidad * p.Precio), 0) AS TotalVendido
FROM Productos p
LEFT JOIN Ventas v ON p.idProductos = v.IdProductos
GROUP BY p.idProductos, p.Nombre, p.Precio;


