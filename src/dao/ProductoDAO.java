package dao;

import config.Conexion;
import modelo.Producto;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static final int UMBRAL_BAJO_STOCK = 10;

    private Producto mapRow(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(rs.getString("categoria"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setEstado(rs.getString("estado"));
        return p;
    }

    public List<Producto> listar() throws SQLException {
        String sql = "SELECT id,codigo,nombre,categoria,precio,stock,estado FROM productos ORDER BY id DESC";
        List<Producto> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public List<Producto> buscar(String texto) throws SQLException {
        String sql = "SELECT id,codigo,nombre,categoria,precio,stock,estado FROM productos WHERE codigo LIKE ? OR nombre LIKE ? ORDER BY id DESC";
        List<Producto> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pat = "%" + texto + "%";
            ps.setString(1, pat);
            ps.setString(2, pat);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public List<Producto> listarBajoStock(int umbral) throws SQLException {
        String sql = "SELECT id,codigo,nombre,categoria,precio,stock,estado FROM productos WHERE stock < ? ORDER BY stock ASC";
        List<Producto> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, umbral);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public Producto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT id,codigo,nombre,categoria,precio,stock,estado FROM productos WHERE codigo=? LIMIT 1";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public Producto buscarPorId(int id) throws SQLException {
        String sql = "SELECT id,codigo,nombre,categoria,precio,stock,estado FROM productos WHERE id=? LIMIT 1";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean existeCodigo(String codigo) throws SQLException {
        return buscarPorCodigo(codigo) != null;
    }

    public boolean existeCodigoExceptoId(String codigo, int idExcluir) throws SQLException {
        String sql = "SELECT 1 FROM productos WHERE codigo=? AND id<>? LIMIT 1";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO productos(codigo,nombre,categoria,precio,stock,estado) VALUES(?,?,?,?,?,?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getEstado());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getInt(1));
            }
        }
    }

    public void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE productos SET codigo=?, nombre=?, categoria=?, precio=?, stock=?, estado=? WHERE id=?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getEstado());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void ajustarStock(int id, int delta) throws SQLException {
        // delta puede ser positivo o negativo, valida que no quede negativo
        String sql = "UPDATE productos SET stock = stock + ? WHERE id=? AND stock + ? >= 0";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setInt(2, id);
            ps.setInt(3, delta);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                // verificar si es por stock negativo o id inexistente
                Producto actual = buscarPorId(id);
                if (actual == null) throw new SQLException("Producto no encontrado (id=" + id + ")");
                if (actual.getStock() + delta < 0) throw new SQLException("Stock no puede quedar negativo. Actual: " + actual.getStock() + ", delta: " + delta);
                throw new SQLException("No se pudo ajustar stock");
            }
        }
    }
}
