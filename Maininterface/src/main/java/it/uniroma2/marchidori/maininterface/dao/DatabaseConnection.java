package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.exception.DatabaseConnectionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String url;
    private static String user;
    private static String password;

    // Costruttore privato per evitare istanziazione esterna
    private DatabaseConnection() {}

    static {
        try (FileInputStream fis = new FileInputStream("src/main/resources/db_config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            // Lancia una nuova eccezione dedicata invece di RuntimeException
            throw new DatabaseConnectionException("❌ Errore nel caricamento della configurazione del database", e);
        }
    }

    // Metodo per ottenere la connessione
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // Lancia una nuova eccezione dedicata in caso di errore nella connessione
            throw new DatabaseConnectionException("❌ Errore nella connessione al database", e);
        }
    }
}
