package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    // 1. Zona Formulario
    public JTextField txtCodigo;
    public JTextField txtNombre;
    public JTextField txtCategoria;
    public JTextField txtPrecio;
    public JTextField txtStock;
    public JComboBox<String> cboEstado;

    // 2. Zona Acciones y Búsqueda
    public JButton btnNuevo;
    public JButton btnGuardar;
    public JButton btnModificar;
    public JButton btnEliminar;
    public JButton btnBuscar;
    public JButton btnBajoStock;
    public JTextField txtBuscar;

    public VentanaPrincipal() {
        setTitle("Gestión de Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        initComponentes();
        setVisible(true);
    }

    private void initComponentes() {
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        add(panelSuperior, BorderLayout.NORTH);

        // --- 1. ZONA FORMULARIO (Arriba) ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField(15);
        txtNombre = new JTextField(15);
        txtCategoria = new JTextField(15);
        txtPrecio = new JTextField(15);
        txtStock = new JTextField(15);
        cboEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Código"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelFormulario.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nombre"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Categoría"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Precio"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Stock"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtStock, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(new JLabel("Estado"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(cboEstado, gbc);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        // --- 2. ZONA ACCIONES Y BÚSQUEDA (Centro) ---
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelAcciones.setBorder(BorderFactory.createTitledBorder("Acciones"));

        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnBuscar = new JButton("Buscar");
        txtBuscar = new JTextField(15);
        btnBajoStock = new JButton("Bajo Stock");

        panelAcciones.add(btnNuevo);
        panelAcciones.add(btnGuardar);
        panelAcciones.add(btnModificar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnBuscar);
        panelAcciones.add(txtBuscar);
        panelAcciones.add(btnBajoStock);

        panelSuperior.add(panelAcciones, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
