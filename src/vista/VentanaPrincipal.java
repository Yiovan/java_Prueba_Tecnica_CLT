package vista;

import controlador.ProductoControlador;
import dao.ProductoDAO;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

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
    // 2b. Ajuste de stock
    public JButton btnStockMas;
    public JButton btnStockMenos;

    // 3. Zona Listado
    public JTable tablaProductos;
    public DefaultTableModel modeloTabla;
    public JScrollPane scrollPane;

    private final ProductoControlador controlador = new ProductoControlador();
    private List<Producto> productosEnTabla;
    private int idSeleccionado = -1;
    private boolean mostrandoBajoStock = false;

    public VentanaPrincipal() {
        setTitle("Gestión de Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        initComponentes();
        initListeners();
        cargarProductos();

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
        panelFormulario.add(new JLabel("Código *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelFormulario.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nombre *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Categoría"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Precio (Gs) *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtPrecio, gbc);

        // Stock con botones +/-
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Stock *"), gbc);
        gbc.gridx = 1;
        JPanel panelStock = new JPanel(new BorderLayout(5, 0));
        panelStock.add(txtStock, BorderLayout.CENTER);
        JPanel panelStockBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        btnStockMas = new JButton("+");
        btnStockMas.setToolTipText("Aumentar stock");
        btnStockMas.setMargin(new Insets(2, 8, 2, 8));
        btnStockMenos = new JButton("-");
        btnStockMenos.setToolTipText("Disminuir stock");
        btnStockMenos.setMargin(new Insets(2, 8, 2, 8));
        panelStockBtns.add(btnStockMas);
        panelStockBtns.add(btnStockMenos);
        panelStock.add(panelStockBtns, BorderLayout.EAST);
        panelFormulario.add(panelStock, gbc);

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
        btnBajoStock = new JButton("Bajo Stock (<" + ProductoDAO.UMBRAL_BAJO_STOCK + ")");

        panelAcciones.add(btnNuevo);
        panelAcciones.add(btnGuardar);
        panelAcciones.add(btnModificar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(new JSeparator(SwingConstants.VERTICAL));
        panelAcciones.add(btnBuscar);
        panelAcciones.add(txtBuscar);
        panelAcciones.add(btnBajoStock);

        panelSuperior.add(panelAcciones, BorderLayout.SOUTH);

        // --- 3. ZONA LISTADO (Abajo) ---
        String[] columnas = {"ID", "Código", "Nombre", "Categoría", "Precio (Gs)", "Stock", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 5) return Integer.class;
                return String.class;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setAutoCreateRowSorter(true);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        // Ocultar columna ID
        tablaProductos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaProductos.getColumnModel().getColumn(0).setWidth(0);
        // Anchos sugeridos
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(60);

        scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Productos"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initListeners() {
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarProducto());
        btnModificar.addActionListener(e -> modificarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnBuscar.addActionListener(e -> buscarProductos());
        btnBajoStock.addActionListener(e -> toggleBajoStock());
        btnStockMas.addActionListener(e -> ajustarStockDialog(true));
        btnStockMenos.addActionListener(e -> ajustarStockDialog(false));

        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) buscarProductos();
            }
        });

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) seleccionarFila();
        });
    }

    // ---- Lógica de tabla ----

    private void cargarProductos() {
        try {
            List<Producto> lista = controlador.listar();
            mostrarLista(lista);
        } catch (Exception ex) {
            mostrarError("Error al listar productos: " + ex.getMessage());
        }
    }

    private String formatGs(int precio) {
        return String.format("%,d", precio).replace(",", ".");
    }

    private void mostrarLista(List<Producto> lista) {
        this.productosEnTabla = lista;
        modeloTabla.setRowCount(0);
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getCodigo(),
                    p.getNombre(),
                    p.getCategoria() != null ? p.getCategoria() : "",
                    p.getPrecio(),
                    p.getStock(),
                    p.getEstado()
            });
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        cboEstado.setSelectedIndex(0);
        tablaProductos.clearSelection();
        txtCodigo.setEnabled(true);
        txtCodigo.requestFocus();
    }

    private void seleccionarFila() {
        int viewRow = tablaProductos.getSelectedRow();
        if (viewRow < 0) return;
        int modelRow = tablaProductos.convertRowIndexToModel(viewRow);
        if (modelRow < 0 || modelRow >= productosEnTabla.size()) return;
        Producto p = productosEnTabla.get(modelRow);
        idSeleccionado = p.getId();
        txtCodigo.setText(p.getCodigo());
        txtNombre.setText(p.getNombre());
        txtCategoria.setText(p.getCategoria() != null ? p.getCategoria() : "");
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
        cboEstado.setSelectedItem(p.getEstado());
    }

    private void guardarProducto() {
        try {
            Producto p = controlador.construirDesdeFormulario(
                    txtCodigo.getText(), txtNombre.getText(), txtCategoria.getText(),
                    txtPrecio.getText(), txtStock.getText(), (String) cboEstado.getSelectedItem());
            controlador.registrar(p);
            mostrarInfo("Producto guardado correctamente");
            limpiarFormulario();
            mostrandoBajoStock = false;
            btnBajoStock.setText("Bajo Stock (<" + ProductoDAO.UMBRAL_BAJO_STOCK + ")");
            cargarProductos();
        } catch (IllegalArgumentException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.toLowerCase().contains("duplicate")) msg = "Código duplicado: ya existe un producto con ese código";
            mostrarError("No se pudo guardar: " + msg);
        }
    }

    private void modificarProducto() {
        if (idSeleccionado <= 0) {
            mostrarAdvertencia("Seleccione un producto del listado para modificar");
            return;
        }
        try {
            Producto p = controlador.construirDesdeFormulario(
                    txtCodigo.getText(), txtNombre.getText(), txtCategoria.getText(),
                    txtPrecio.getText(), txtStock.getText(), (String) cboEstado.getSelectedItem());
            p.setId(idSeleccionado);
            controlador.modificar(p);
            mostrarInfo("Producto modificado correctamente");
            cargarProductos();
            // re-seleccionar el modificado
            seleccionarPorId(idSeleccionado);
        } catch (IllegalArgumentException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.toLowerCase().contains("duplicate")) msg = "Código duplicado: otro producto usa ese código";
            mostrarError("No se pudo modificar: " + msg);
        }
    }

    private void eliminarProducto() {
        if (idSeleccionado <= 0) {
            mostrarAdvertencia("Seleccione un producto del listado para eliminar");
            return;
        }
        int viewRow = tablaProductos.getSelectedRow();
        if (viewRow < 0) return;
        int modelRow = tablaProductos.convertRowIndexToModel(viewRow);
        Producto p = productosEnTabla.get(modelRow);
        int opt = JOptionPane.showConfirmDialog(this,
                "¿Eliminar producto '" + p.getCodigo() + " - " + p.getNombre() + "'?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;
        try {
            controlador.eliminar(idSeleccionado);
            mostrarInfo("Producto eliminado");
            limpiarFormulario();
            cargarProductos();
        } catch (Exception ex) {
            mostrarError("No se pudo eliminar: " + ex.getMessage());
        }
    }

    private void buscarProductos() {
        String texto = txtBuscar.getText() != null ? txtBuscar.getText().trim() : "";
        try {
            List<Producto> lista = controlador.buscar(texto);
            mostrandoBajoStock = false;
            btnBajoStock.setText("Bajo Stock (<" + ProductoDAO.UMBRAL_BAJO_STOCK + ")");
            mostrarLista(lista);
            if (lista.isEmpty()) mostrarInfo("Sin resultados para: " + texto);
        } catch (Exception ex) {
            mostrarError("Error al buscar: " + ex.getMessage());
        }
    }

    private void toggleBajoStock() {
        try {
            if (!mostrandoBajoStock) {
                List<Producto> lista = controlador.listarBajoStock();
                mostrarLista(lista);
                mostrandoBajoStock = true;
                btnBajoStock.setText("Ver Todos");
                if (lista.isEmpty()) mostrarInfo("No hay productos con stock < " + ProductoDAO.UMBRAL_BAJO_STOCK);
            } else {
                cargarProductos();
                mostrandoBajoStock = false;
                btnBajoStock.setText("Bajo Stock (<" + ProductoDAO.UMBRAL_BAJO_STOCK + ")");
            }
        } catch (Exception ex) {
            mostrarError("Error al consultar bajo stock: " + ex.getMessage());
        }
    }

    private void ajustarStockDialog(boolean aumentar) {
        if (idSeleccionado <= 0) {
            mostrarAdvertencia("Seleccione un producto del listado para ajustar stock");
            return;
        }
        String accion = aumentar ? "aumentar" : "disminuir";
        String input = JOptionPane.showInputDialog(this,
                "Cantidad a " + accion + " (entero positivo):", "Ajustar Stock", JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;
        input = input.trim();
        if (input.isEmpty()) return;
        int cantidad;
        try {
            cantidad = Integer.parseInt(input);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            mostrarAdvertencia("Cantidad inválida: debe ser entero positivo");
            return;
        }
        int delta = aumentar ? cantidad : -cantidad;
        try {
            controlador.ajustarStock(idSeleccionado, delta);
            mostrarInfo("Stock ajustado: " + (delta > 0 ? "+" : "") + delta);
            // refrescar manteniendo filtro actual
            if (mostrandoBajoStock) {
                List<Producto> lista = controlador.listarBajoStock();
                mostrarLista(lista);
            } else {
                cargarProductos();
            }
            seleccionarPorId(idSeleccionado);
            // actualizar campo stock visual
            for (Producto p : productosEnTabla) if (p.getId() == idSeleccionado) txtStock.setText(String.valueOf(p.getStock()));
        } catch (Exception ex) {
            mostrarError("No se pudo ajustar stock: " + ex.getMessage());
        }
    }

    private void seleccionarPorId(int id) {
        for (int i = 0; i < productosEnTabla.size(); i++) {
            if (productosEnTabla.get(i).getId() == id) {
                int viewIdx = tablaProductos.convertRowIndexToView(i);
                if (viewIdx >= 0) tablaProductos.setRowSelectionInterval(viewIdx, viewIdx);
                break;
            }
        }
    }

    private void mostrarInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    private void mostrarAdvertencia(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }
    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
