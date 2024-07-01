package inventario_almacen;

import java.sql.*;

public class InventarioModel {

    // Método para obtener productos según el estado
    public static ResultSet getProducts(boolean active) throws SQLException {
        Connection con = Conexion.getConnection();
        String query = "SELECT p.idProducto, p.Nombre, p.Descripcion, p.Precio, i.Cantidad, p.Estatus " +
                       "FROM Productos p LEFT JOIN Inventario i ON p.idProducto = i.idProducto WHERE p.Estatus=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setBoolean(1, active);
        return ps.executeQuery();
    }
    
   public static ResultSet getAllProducts() throws SQLException {
        String query = "SELECT p.idProducto, p.Nombre, p.Descripcion, p.Precio, i.Cantidad, p.Estatus " +
                       "FROM Productos p " +
                       "LEFT JOIN Inventario i ON p.idProducto = i.idProducto";
        Connection con = Conexion.getConnection();
        Statement stmt = con.createStatement();
        return stmt.executeQuery(query);
    }

    // Método para añadir nuevo producto
    public static void addProduct(String nombre, String descripcion, double precio) throws SQLException {
        Connection con = Conexion.getConnection();
        String query = "INSERT INTO Productos (Nombre, Descripcion, Precio, Estatus) VALUES (?, ?, ?, TRUE)";
        PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setDouble(3, precio);
        ps.executeUpdate();

        // Obtener ID del nuevo producto
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int productId = rs.getInt(1);

            // Añadir entrada en inventario
            query = "INSERT INTO Inventario (idProducto, Cantidad) VALUES (?, 0)";
            ps = con.prepareStatement(query);
            ps.setInt(1, productId);
            ps.executeUpdate();
        }
    }

    // Método para modificar inventario
public static boolean modifyInventory(int idProducto, int cantidad, boolean isEntry) throws SQLException {
    Connection con = Conexion.getConnection();
    con.setAutoCommit(false); // Iniciar transacción

    // Verificar la cantidad actual
    String checkQuery = "SELECT Cantidad FROM Inventario WHERE idProducto = ?";
    PreparedStatement checkPs = con.prepareStatement(checkQuery);
    checkPs.setInt(1, idProducto);
    ResultSet rs = checkPs.executeQuery();

    if (rs.next()) {
        int currentQuantity = rs.getInt("Cantidad");

        if (!isEntry && currentQuantity < cantidad) {
            con.rollback(); // Revertir transacción si no hay suficiente inventario
            return false; // No se puede restar más de lo que hay
        }

        String updateQuery = "UPDATE Inventario SET Cantidad = Cantidad + ? WHERE idProducto = ?";
        if (!isEntry) {
            updateQuery = "UPDATE Inventario SET Cantidad = Cantidad - ? WHERE idProducto = ?";
        }

        PreparedStatement updatePs = con.prepareStatement(updateQuery);
        updatePs.setInt(1, cantidad);
        updatePs.setInt(2, idProducto);
        updatePs.executeUpdate();

        con.commit(); // Confirmar transacción
        return true;
    } else {
        con.rollback(); // Revertir transacción si no se encuentra el producto
        return false;
    }
}
    public static boolean checkProductExists(int idProducto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            con = Conexion.getConnection();
            String query = "SELECT COUNT(*) AS count FROM Productos WHERE idProducto = ? AND Estatus = TRUE";
            ps = con.prepareStatement(query);
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                if (count > 0) {
                    exists = true;
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }

        return exists;
    }
public static void recordMovement(int idInventario, int idUsuario, String tipoMovimiento, int cantidad) throws SQLException {
    // Verificar primero si el idUsuario existe en la tabla usuarios
    if (!InventarioModel.checkUsuarioExists(idUsuario)) {
        throw new SQLException("El idUsuario no existe en la tabla usuarios");
    }

    // Si el idUsuario existe, proceder con el registro del movimiento
    String query = "INSERT INTO Movimientos (idInventario, idUsuario, TipoMovimiento, Cantidad) VALUES (?, ?, ?, ?)";
    Connection con = Conexion.getConnection();
    PreparedStatement pstmt = con.prepareStatement(query);
    pstmt.setInt(1, idInventario);
    pstmt.setInt(2, idUsuario);
    pstmt.setString(3, tipoMovimiento);
    pstmt.setInt(4, cantidad);
    pstmt.executeUpdate();
}

public static boolean checkUsuarioExists(int idUsuario) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean exists = false;

    try {
        con = Conexion.getConnection();
        String query = "SELECT COUNT(*) AS count FROM usuarios WHERE idUsuario = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, idUsuario);
        rs = ps.executeQuery();

        if (rs.next()) {
            int count = rs.getInt("count");
            exists = (count > 0);
        }
    } finally {
        if (rs != null) rs.close();
        if (ps != null) ps.close();
        if (con != null) con.close();
    }

    return exists;
}
    // Método para obtener la cantidad actual de un producto
    public static int getProductQuantity(int idProducto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cantidad = 0;

        try {
            con = Conexion.getConnection();
            String query = "SELECT Cantidad FROM Inventario WHERE idProducto = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();

            if (rs.next()) {
                cantidad = rs.getInt("Cantidad");
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }

        return cantidad;
    }


    // Método para cambiar el estatus de un producto
    public static void toggleProductStatus(int idProducto, boolean status) throws SQLException {
        Connection con = Conexion.getConnection();
        String query = "UPDATE Productos SET Estatus = ? WHERE idProducto = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setBoolean(1, status);
        ps.setInt(2, idProducto);
        ps.executeUpdate();
    }

    // Método para obtener el ID del usuario actual (esto es un placeholder, actualiza según tu lógica)
    private static int getCurrentUserId() {
        // Aquí debes retornar el ID del usuario que ha iniciado sesión
        return 1; // Placeholder para el usuario actual
    }
}
