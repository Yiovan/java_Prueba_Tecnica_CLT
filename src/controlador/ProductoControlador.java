package controlador;

import dao.ProductoDAO;
import modelo.Producto;

import java.sql.SQLException;
import java.util.List;

public class ProductoControlador {

    private final ProductoDAO dao = new ProductoDAO();

    public List<Producto> listar() throws SQLException {
        return dao.listar();
    }

    public List<Producto> buscar(String texto) throws SQLException {
        if (texto == null || texto.isBlank()) return dao.listar();
        return dao.buscar(texto.trim());
    }

    public List<Producto> listarBajoStock() throws SQLException {
        return dao.listarBajoStock(ProductoDAO.UMBRAL_BAJO_STOCK);
    }

    public void registrar(Producto p) throws Exception {
        validar(p, false);
        if (dao.existeCodigo(p.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con código '" + p.getCodigo() + "'");
        }
        dao.insertar(p);
    }

    public void modificar(Producto p) throws Exception {
        if (p.getId() <= 0) throw new IllegalArgumentException("Seleccione un producto para modificar");
        validar(p, true);
        if (dao.existeCodigoExceptoId(p.getCodigo(), p.getId())) {
            throw new IllegalArgumentException("Código duplicado: otro producto usa '" + p.getCodigo() + "'");
        }
        dao.actualizar(p);
    }

    public void eliminar(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Seleccione un producto para eliminar");
        dao.eliminar(id);
    }

    public void ajustarStock(int id, int delta) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Seleccione un producto");
        if (delta == 0) throw new IllegalArgumentException("Delta no puede ser 0");
        dao.ajustarStock(id, delta);
    }

    private void validar(Producto p, boolean esActualizacion) throws IllegalArgumentException {
        if (p.getCodigo() == null || p.getCodigo().isBlank())
            throw new IllegalArgumentException("Código es obligatorio");
        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new IllegalArgumentException("Nombre es obligatorio");
        if (p.getPrecio() <= 0)
            throw new IllegalArgumentException("Precio debe ser mayor a 0 (Gs)");
        if (p.getStock() < 0)
            throw new IllegalArgumentException("Stock no puede ser negativo");
        if (p.getCodigo().length() > 50)
            throw new IllegalArgumentException("Código máximo 50 caracteres");
        if (p.getNombre().length() > 100)
            throw new IllegalArgumentException("Nombre máximo 100 caracteres");
        // categoria y estado opcionales pero validar
        if (p.getCategoria() != null && p.getCategoria().length() > 50)
            throw new IllegalArgumentException("Categoría máximo 50 caracteres");
        if (p.getEstado() == null || p.getEstado().isBlank()) p.setEstado("Activo");
        if (!p.getEstado().equals("Activo") && !p.getEstado().equals("Inactivo"))
            throw new IllegalArgumentException("Estado debe ser Activo o Inactivo");
    }

    public Producto construirDesdeFormulario(String codigo, String nombre, String categoria, String precioStr, String stockStr, String estado) throws Exception {
        String cod = codigo != null ? codigo.trim() : "";
        String nom = nombre != null ? nombre.trim() : "";
        String cat = categoria != null ? categoria.trim() : "";
        if (cat.isEmpty()) cat = null;
        int precio;
        try {
            // Permite Gs con puntos o comas como separador de miles (ej: 1.500.000)
            String clean = precioStr.trim().replace(".", "").replace(",", "").replace(" ", "");
            precio = Integer.parseInt(clean);
        } catch (Exception e) {
            throw new IllegalArgumentException("Precio inválido (Gs, entero): " + precioStr);
        }
        int stock;
        try {
            stock = Integer.parseInt(stockStr.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Stock inválido: " + stockStr + " (debe ser entero)");
        }
        return new Producto(cod, nom, cat, precio, stock, estado);
    }
}
