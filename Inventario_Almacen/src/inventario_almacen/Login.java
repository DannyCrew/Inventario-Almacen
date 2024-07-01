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

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContraseña);
        panel.add(btnLogin);

        add(panel);

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
