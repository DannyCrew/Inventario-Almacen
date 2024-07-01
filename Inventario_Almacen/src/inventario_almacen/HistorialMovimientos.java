package inventario_almacen;
/**
 *
 * @author Danny
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class HistorialMovimientos extends JFrame {

    private JTable table = new JTable();
    private DefaultTableModel model = new DefaultTableModel();
    private JButton btnActualizar = new JButton("Actualizar");

    public HistorialMovimientos() {
        setTitle("Historial de Movimientos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(btnActualizar, BorderLayout.EAST);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Configurar modelo de tabla
        String[] columns = {"Fecha y Hora", "Tipo de Movimiento", "ID Inventario", "ID Usuario", "Nombre Producto", "Cantidad"};
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        // Acción para el botón de actualización
        btnActualizar.addActionListener(e -> {
            try {
                mostrarMovimientos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al obtener el historial de movimientos: " + ex.getMessage());
            }
        });

        // Mostrar todos los movimientos al iniciar la ventana
        try {
            mostrarMovimientos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener el historial de movimientos: " + ex.getMessage());
        }
    }

    private void mostrarMovimientos() throws SQLException {
        // Limpiar tabla antes de mostrar nuevos datos
        model.setRowCount(0);

        // Obtener todos los movimientos de la base de datos
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            con = Conexion.getConnection();
            statement = con.createStatement();

            // Consulta SQL ajustada para unir con Productos y obtener el nombre del producto
            String query = "SELECT m.FechaHora, m.TipoMovimiento, m.idInventario, m.idUsuario, p.Nombre AS NombreProducto, m.Cantidad " +
                           "FROM Movimientos m " +
                           "JOIN Inventario i ON m.idInventario = i.idInventario " +
                           "JOIN Productos p ON i.idProducto = p.idProducto";

            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String fechaHora = resultSet.getString("FechaHora");
                String tipoMovimiento = resultSet.getString("TipoMovimiento");
                int idInventario = resultSet.getInt("idInventario");
                int idUsuario = resultSet.getInt("idUsuario");
                String nombreProducto = resultSet.getString("NombreProducto");
                int cantidad = resultSet.getInt("Cantidad");

                model.addRow(new Object[]{fechaHora, tipoMovimiento, idInventario, idUsuario, nombreProducto, cantidad});
            }
        } finally {
            // Cerrar ResultSet, Statement y Connection
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HistorialMovimientos historialMovimientos = new HistorialMovimientos();
            historialMovimientos.setVisible(true);
        });
    }
}
