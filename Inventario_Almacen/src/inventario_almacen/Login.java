package inventario_almacen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private int idUsuario; // Variable para almacenar el idUsuario
    private String rolUsuario;

    public Login() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtEmail = new JTextField(15);
        txtContraseña = new JPasswordField(15);
        btnLogin = new JButton("Login");

         // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245)); // Fondo claro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margen entre componentes

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        lblEmail.setForeground(new Color(0, 102, 204)); // Azul oscuro

        txtEmail = new JTextField(15);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204))); // Borde azul

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Arial", Font.BOLD, 14));
        lblContraseña.setForeground(new Color(0, 102, 204));

        txtContraseña = new JPasswordField(15);
        txtContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContraseña.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblEmail, gbc);

        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblContraseña, gbc);

        gbc.gridx = 1;
        panel.add(txtContraseña, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(btnLogin, gbc);

        add(panel, BorderLayout.CENTER);

        btnLogin.addActionListener((ActionEvent e) -> {
            login();
        });
    }

    private void login() {
        String email = txtEmail.getText();
        String contraseña = new String(txtContraseña.getPassword());

        if (email.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos");
            return;
        }

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT idUsuario, idRol FROM Usuarios WHERE Email=? AND Contraseña=?")) {

            ps.setString(1, email);
            ps.setString(2, contraseña);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idUsuario = rs.getInt("idUsuario"); // Almacenar el idUsuario
                    int idRol = rs.getInt("idRol");
                    rolUsuario = (idRol == 1) ? "Administrador" : "Almacenista"; // Determinar el rol

                    JOptionPane.showMessageDialog(this, "Login exitoso");
                    this.dispose();

                    // Mostrar el inventario después del login
                    Inventario inventario = new Inventario();
                    inventario.setIdUsuario(idUsuario); // Pasar el idUsuario al inventario
                    inventario.setRolUsuario(rolUsuario); // Establecer el rol del usuario en el inventario
                    inventario.setVisible(true);
                    InventarioController.viewAllProducts(inventario);
                } else {
                    JOptionPane.showMessageDialog(this, "Email o contraseña incorrectos");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
