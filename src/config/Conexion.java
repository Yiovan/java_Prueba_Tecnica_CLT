package config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./")
            .ignoreIfMissing()
            .load();

    private static String getEnv(String key, String defaultValue) {
        String v = dotenv.get(key);
        if (v == null || v.isBlank()) v = System.getenv(key);
        return v != null && !v.isBlank() ? v : defaultValue;
    }

    private static final String DB_NAME = getEnv("DB_NAME", "gestion_productos");
    private static final String DB_PASSWORD = getEnv("DB_PASSWORD", "root");
    private static final String DB_PORT = getEnv("DB_PORT", "3306");
    private static final String DB_HOST = getEnv("DB_HOST", "localhost");
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = DB_PASSWORD;

    public static Connection conectar() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión a la base de datos exitosa! " + URL.replace(PASSWORD, "***"));
            return conexion;
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage() + " URL=" + URL);
            return null;
        }
    }

    public static String getUrl() { return URL; }

    public static void main(String[] args) {
        try (Connection c = conectar()) {
            if (c != null && !c.isClosed()) System.out.println("Conexión válida");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}