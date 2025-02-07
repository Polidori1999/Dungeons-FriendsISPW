package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.dao.DatabaseConnection;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneSwitcher.changeScene(primaryStage, SceneNames.LOGIN, null);
    }

    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                logger.info("✅ Connessione a MySQL riuscita!");
                conn.close();
            } else {
                logger.info("❌ Connessione fallita.");
            }
        } catch (SQLException e) {
            logger.severe("❌ Errore di connessione: " + e.getMessage());
        }
        launch();
    }
}
