package config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Cargamos el archivo .env
    private static final Dotenv dotenv = Dotenv.configure().directory("./").load();
    // Armamos los datos leyendo el .env
    // Asegúrate de que DB_NAME y DB_PASSWORD existan en tu archivo .env
    private static final String URL = "jdbc:mysql://localhost:3307/" + dotenv.get("DB_NAME") + "?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión a la base de datos exitosa usando .env!");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    public static void main(String[] args) {
        conectar();
    }
}