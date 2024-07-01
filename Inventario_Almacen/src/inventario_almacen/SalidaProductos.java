package inventario_almacen;
/**
 *
 * @author Danny
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalidaProductos extends JFrame {

    private JTable table = new JTable();
    private DefaultTableModel model = new DefaultTableModel();
    private JTextField txtIdProducto = new JTextField(10);
    private JTextField txtCantidad = new JTextField(10);
    private JButton btnReducirInventario = new JButton("Reducir Inventario");

    private int idUsuario; // Variable para almacenar el idUsuario

    public SalidaProductos(int idUsuario) {
        this.idUsuario = idUsuario; // Inicializar el idUsuario
        initComponents();
    }

    private void initComponents() {
        setTitle("Salida de Productos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panelForm = new JPanel(new FlowLayout());
        panelForm.add(new JLabel("ID Producto:"));
        panelForm.add(txtIdProducto);
        panelForm.add(new JLabel("Cantidad a reducir:"));
        panelForm.add(txtCantidad);
        panelForm.add(btnReducirInventario);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelForm, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Configurar modelo de tabla
        String[] columns = {"ID", "Nombre", "Descripción", "Precio", "Cantidad", "Estado"};
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        // Acción del botón para reducir inventario
        btnReducirInventario.addActionListener((ActionEvent e) -> {
            reducirInventario();
        });

        // Mostrar todos los productos activos al iniciar la ventana
        viewActiveProducts();

        // Actualizar la tabla cada vez que se muestra la ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent windowEvent) {
                viewActiveProducts();
            }
        });
    }

    private void reducirInventario() {
        String idProductoStr = txtIdProducto.getText();
        String cantidadStr = txtCantidad.getText();

        try {
            int idProducto = Integer.parseInt(idProductoStr);
            int cantidad = Integer.parseInt(cantidadStr);

            // Validar que la cantidad sea mayor que cero
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero");
                return;
            }

            // Verificar si el producto existe y está activo
            if (!InventarioModel.checkProductExists(idProducto)) {
                JOptionPane.showMessageDialog(this, "El producto no existe o no está activo");
                return;
            }

            // Obtener la cantidad actual del producto
            int cantidadActual = InventarioModel.getProductQuantity(idProducto);

            // Validar que la cantidad a reducir no sea mayor que la cantidad actual
            if (cantidad > cantidadActual) {
                JOptionPane.showMessageDialog(this, "No puedes reducir más de la cantidad actual del producto");
                return;
            }

            // Reducir el inventario del producto
            boolean resultado = InventarioModel.modifyInventory(idProducto, cantidad, false);
            if (resultado) {
                JOptionPane.showMessageDialog(this, "Inventario reducido correctamente");
                // Registrar movimiento de salida
                registerSalidaMovement(idProducto, cantidad);
                // Actualizar la tabla de productos activos después de la reducción
                viewActiveProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Error al reducir el inventario del producto");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID de producto o cantidad inválidos");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al acceder a la base de datos: " + ex.getMessage());
        }
    }

    private void registerSalidaMovement(int idProducto, int cantidad) {
        try {
            // Registrar movimiento de salida con el idUsuario almacenado
            InventarioModel.recordMovement(idProducto, idUsuario, "Salida", cantidad);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar movimiento de salida: " + ex.getMessage());
        }
    }

    private void viewActiveProducts() {
        try {
            model.setRowCount(0); // Limpiar tabla
            ResultSet rs = InventarioModel.getProducts(true); // Obtener solo productos activos
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("idProducto"),
                        rs.getString("Nombre"),
                        rs.getString("Descripcion"),
                        rs.getDouble("Precio"),
                        rs.getInt("Cantidad"),
                        "Activo"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener productos activos: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int idUsuario = 1; // Obtener el idUsuario del usuario logueado
            new SalidaProductos(idUsuario).setVisible(true);
        });
    }
}
