package modelo;

public class Producto {
    private int id;
    private String codigo;
    private String nombre;
    private String categoria;
    private int precio; // Gs, sin decimales
    private int stock;
    private String estado;

    public Producto() {}

    public Producto(int id, String codigo, String nombre, String categoria, int precio, int stock, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    public Producto(String codigo, String nombre, String categoria, int precio, int stock, String estado) {
        this(0, codigo, nombre, categoria, precio, stock, estado);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
