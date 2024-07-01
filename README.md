# Inventario-Almacen

Este repositorio contiene un sistema de gestión de inventario desarrollado en Java para una aplicación de almacenamiento. Incluye funcionalidades como registro de productos, gestión de inventario, roles de usuario (administrador y almacenista), y acceso diferenciado a funciones basado en el rol del usuario.

## Características Principales

- Gestión completa de productos y su estado (activo/inactivo).
- Registro de movimientos de entrada y salida de inventario.
- Acceso diferenciado para administradores y almacenistas:
  - **Administradores**: Acceso completo a todas las funciones, incluyendo el historial de movimientos.
  - **Almacenistas**: Restricciones en funciones como aumentar inventario y acceso limitado al historial de movimientos.

## Tecnologías Utilizadas
 ![Java Icon](https://img.icons8.com/color/48/000000/java-coffee-cup-logo--v1.png)
  ![MySQL Icon](https://img.icons8.com/ios-filled/50/000000/mysql-logo.png)
   ![Swing Icon](https://img.icons8.com/nolan/64/java-eclipse.png)
   ![JDBC Icon](https://img.icons8.com/color/48/000000/database.png)
- Java
- MySQL
- Swing (para la interfaz gráfica) 
- JDBC (para la conexión a la base de datos) 


## Instrucciones de Uso

1. Clona el repositorio en tu máquina local.
2. Configura la base de datos MySQL según el esquema proporcionado.
3. Ejecuta la aplicación desde la clase Login para iniciar sesión.
4. Explora las funcionalidades según tu rol de usuario (administrador o almacenista).
