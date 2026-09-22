package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    // Campos del formulario
    public JTextField txtCodigo;
    public JTextField txtNombre;
    public JTextField txtCategoria;
    public JTextField txtPrecio;
    public JTextField txtStock;
    public JComboBox<String> cboEstado;

    public VentanaPrincipal() {
        setTitle("Gestión de Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
