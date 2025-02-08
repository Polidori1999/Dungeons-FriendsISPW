package it.uniroma2.marchidori.maininterface.dao;

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

    static {
        try (FileInputStream fis = new FileInputStream("src/main/resources/db_config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("❌ Errore nel caricamento della configurazione del database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
