package inventario_almacen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventarioController {
    private static boolean movimientoRegistrado; 
    
    public static void addProduct(JFrame parent, String rolUsuario) {
        if (rolUsuario.equals("Administrador")) {
        String nombre = JOptionPane.showInputDialog(parent, "Nombre del Producto:");
        String descripcion = JOptionPane.showInputDialog(parent, "Descripción del Producto:");
        String precioStr = JOptionPane.showInputDialog(parent, "Precio del Producto:");
        

        try {
            double precio = Double.parseDouble(precioStr);
            InventarioModel.addProduct(nombre, descripcion, precio);
            JOptionPane.showMessageDialog(parent, "Producto añadido correctamente");
            viewAllProducts((Inventario) parent); // Actualizar la vista
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Precio inválido");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parent, "Error al añadir el producto: " + ex.getMessage());
        }
        } else {
            JOptionPane.showMessageDialog(parent, "No tienes permiso para agregar productos");
        }
    }

public static void modifyInventory(JFrame parent, int idUsuario, String rolUsuario) {
    if (rolUsuario.equals("Administrador") || rolUsuario.equals("Almacenista")) {
        String idStr = JOptionPane.showInputDialog(parent, "ID del Producto:");
        String cantidadStr = JOptionPane.showInputDialog(parent, "Cantidad a aumentar:");

    try {
        int idProducto = Integer.parseInt(idStr);
        int cantidad = Integer.parseInt(cantidadStr);

        // Validar que la cantidad sea mayor que cero
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(parent, "La cantidad debe ser mayor que cero");
            return;
        }
         // Modificar el inventario (aumentar la cantidad)
        boolean result = InventarioModel.modifyInventory(idProducto, cantidad, true);
        if (result) {
                // Registrar el movimiento en la tabla Movimientos
                InventarioModel.recordMovement(idProducto, idUsuario, "Entrada", cantidad);
                movimientoRegistrado = true;
            JOptionPane.showMessageDialog(parent, "Inventario aumentado correctamente");
            viewAllProducts((Inventario) parent); // Actualizar la vista de productos
        } else {
            JOptionPane.showMessageDialog(parent, "Error al aumentar el inventario.");
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(parent, "ID o cantidad inválidos");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(parent, "Error al modificar el inventario: " + ex.getMessage());
    }
    } else {
            JOptionPane.showMessageDialog(parent, "No tienes permiso para modificar el inventario");
        }
}

    public static void toggleProductStatus(JFrame parent, String rolUsuario) {
        String idStr = JOptionPane.showInputDialog(parent, "ID del Producto:");
        if (rolUsuario.equals("Administrador")) {
        try {
            int idProducto = Integer.parseInt(idStr);
            int currentStatus = JOptionPane.showConfirmDialog(parent, "¿Activar el producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            boolean status = (currentStatus == JOptionPane.YES_OPTION);

            InventarioModel.toggleProductStatus(idProducto, status);
            JOptionPane.showMessageDialog(parent, "Estado del producto cambiado correctamente");
            viewAllProducts((Inventario) parent); // Actualizar la vista
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "ID inválido");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parent, "Error al cambiar el estado del producto: " + ex.getMessage());
        }
        } else {
            JOptionPane.showMessageDialog(parent, "No tienes permiso para cambiar el estado del producto");
        }
    }

     public static void viewActiveProducts(Inventario view) {
        try {
            DefaultTableModel model = view.getTableModel();
            model.setRowCount(0); // Limpiar tabla
            ResultSet rs = InventarioModel.getProducts(true); // Obtener productos activos
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("idProducto"),
                    rs.getString("Nombre"),
                    rs.getString("Descripcion"),
                    rs.getDouble("Precio"),
                    rs.getInt("Cantidad"),
                    rs.getBoolean("Estatus") ? "Activo" : "Inactivo"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Error al obtener productos activos: " + ex.getMessage());
        }
    }

    public static void viewInactiveProducts(Inventario view) {
        try {
            DefaultTableModel model = view.getTableModel();
            model.setRowCount(0); // Limpiar tabla
            ResultSet rs = InventarioModel.getProducts(false); // Obtener productos inactivos
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("idProducto"),
                    rs.getString("Nombre"),
                    rs.getString("Descripcion"),
                    rs.getDouble("Precio"),
                    rs.getInt("Cantidad"),
                    rs.getBoolean("Estatus") ? "Activo" : "Inactivo"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Error al obtener productos inactivos: " + ex.getMessage());
        }
    }
 public static void viewAllProducts(Inventario view) {
        try {
            DefaultTableModel model = view.getTableModel();
            model.setRowCount(0); // Limpiar la tabla

            // Obtener todos los productos
            ResultSet rsAllProducts = InventarioModel.getAllProducts();

            // Recorrer el resultado y agregar filas al modelo de la tabla
            while (rsAllProducts.next()) {
                model.addRow(new Object[]{
                        rsAllProducts.getInt("idProducto"),
                        rsAllProducts.getString("Nombre"),
                        rsAllProducts.getString("Descripcion"),
                        rsAllProducts.getDouble("Precio"),
                        rsAllProducts.getInt("Cantidad"), // Asegúrate de que esta columna existe ahora
                        rsAllProducts.getBoolean("Estatus") ? "Activo" : "Inactivo"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Error al obtener productos: " + ex.getMessage());
            ex.printStackTrace(); // Imprimir el rastreo de la excepción para depuración
        }
    }    
}
