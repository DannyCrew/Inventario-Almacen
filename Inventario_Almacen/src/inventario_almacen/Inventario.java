package inventario_almacen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Inventario extends JFrame {
    private int idUsuario;
    private String rolUsuario;
    private JTable table = new JTable();
    private DefaultTableModel model = new DefaultTableModel();
    private JButton btnAgregarProducto = new JButton("Agregar Producto");
    private JButton btnAumentarInventario = new JButton("Aumentar Inventario");
    private JButton btnDarDeBaja = new JButton("Cambiar estado");
    private JButton btnVerActivos = new JButton("Ver Activos");
    private JButton btnVerInactivos = new JButton("Ver Inactivos");
    private JButton btnVerTodos = new JButton("Ver Todos");
    private JButton btnSalidaProductos = new JButton("Salida de Productos");
    private JButton btnHistorialMovimientos = new JButton("Historial Movimientos");

    private JPanel panelButtons = new JPanel();


    public Inventario() {
        setTitle("Inventario");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);

        panelButtons.setLayout(new GridLayout(1, 0, 5, 5)); // Inicialmente en una fila
        panelButtons.add(btnAgregarProducto);
        panelButtons.add(btnAumentarInventario);
        panelButtons.add(btnDarDeBaja);
        panelButtons.add(btnVerActivos);
        panelButtons.add(btnVerInactivos);
        panelButtons.add(btnVerTodos);
        panelButtons.add(btnSalidaProductos);
        panelButtons.add(btnHistorialMovimientos);

        // Ajustar el tamaño preferido de los botones
        adjustButtonSizes();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelButtons, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Configurar modelo de tabla
        String[] columns = {"ID", "Nombre", "Descripción", "Precio", "Cantidad", "Estado"};
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        // Acciones de los botones
        btnAgregarProducto.addActionListener((ActionEvent e) -> {
            if ("Administrador".equals(getRolUsuario())) {
            InventarioController.addProduct(this, rolUsuario);
            } else {
                JOptionPane.showMessageDialog(this, "Acceso denegado para Almacenistas");
            }
        });

        btnAumentarInventario.addActionListener((ActionEvent e) -> {
            if ("Administrador".equals(getRolUsuario())) {
            InventarioController.modifyInventory(this, getIdUsuario(), rolUsuario);
            } else {
                JOptionPane.showMessageDialog(this, "Acceso denegado para Almacenistas");
            }
        });

        btnDarDeBaja.addActionListener((ActionEvent e) -> {
            if ("Administrador".equals(getRolUsuario())) {
            InventarioController.toggleProductStatus(this, rolUsuario);
            } else {
                JOptionPane.showMessageDialog(this, "Acceso denegado para Almacenistas");
            }
        });

        btnVerActivos.addActionListener((ActionEvent e) -> {
            InventarioController.viewActiveProducts(this);
        });

        btnVerInactivos.addActionListener((ActionEvent e) -> {
            InventarioController.viewInactiveProducts(this);
        });

        btnVerTodos.addActionListener((ActionEvent e) -> {
            InventarioController.viewAllProducts(this);
        });

        btnSalidaProductos.addActionListener((ActionEvent e) -> {
            SalidaProductos salidaProductos = new SalidaProductos(idUsuario);
            salidaProductos.setVisible(true);
        });

       btnHistorialMovimientos.addActionListener((ActionEvent e) -> {
            if ("Administrador".equals(getRolUsuario())) { // Verificar el rol del usuario en la instancia actual
                HistorialMovimientos abrirHistorialMovimientos = new HistorialMovimientos();
                abrirHistorialMovimientos.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No tienes permisos para acceder al historial de movimientos.");
            }
        });


        // Mostrar productos activos al inicio
        InventarioController.viewAllProducts(this);

        // Ajustar el diseño del panel de botones cuando la ventana cambia de tamaño
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustButtonPanelLayout();
            }
        });

        // Ajustar el tamaño de la ventana para ser dinámico
        setMinimumSize(new Dimension(800, 600)); // Establecer tamaño mínimo adecuado
        setResizable(true); // Permitir que el usuario ajuste el tamaño de la ventana
    }

    private void adjustButtonSizes() {
        JButton[] buttons = {
            btnAgregarProducto, btnAumentarInventario, btnDarDeBaja,
            btnVerActivos, btnVerInactivos, btnVerTodos,
            btnSalidaProductos, btnHistorialMovimientos
        };

        // Obtener el ancho máximo
        int maxWidth = 0;
        int maxHeight = 0;
        for (JButton button : buttons) {
            Dimension size = button.getPreferredSize();
            if (size.width > maxWidth) {
                maxWidth = size.width;
            }
            if (size.height > maxHeight) {
                maxHeight = size.height;
            }
        }

        // Ajustar el tamaño de todos los botones al tamaño máximo
        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(maxWidth, maxHeight));
        }
    }

    private void adjustButtonPanelLayout() {
        int frameWidth = getWidth();
        // Decidir cuántas filas usar en función del ancho de la ventana
        int numRows = (frameWidth < 800) ? 2 : 1;
        panelButtons.setLayout(new GridLayout(numRows, 0, 5, 5));
        panelButtons.revalidate(); // Volver a validar el panel para aplicar el nuevo diseño
    }

    public DefaultTableModel getTableModel() {
        return model;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
