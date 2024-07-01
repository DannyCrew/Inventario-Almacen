package inventario_almacen;
/**
 *
 * @author Danny
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Danny
 */
public class Conexion {
public static final String URL = "jdbc:mysql://localhost:3306/pruebajava";
public static final String USER = "root";
public static final String CLAVE = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, CLAVE);
    }
}

