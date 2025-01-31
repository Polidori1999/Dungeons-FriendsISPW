package it.uniroma2.marchidori.maininterface.sceneManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {

    /**
     * Cambia la scena caricando il file FXML specificato.
     * @param currentStage Lo stage corrente su cui cambiare la scena.
     * @param fxml Il percorso del file FXML da caricare.
     * @throws IOException Se c'è un errore durante il caricamento del file FXML.
     */
    public static void changeScene(Stage currentStage, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/it/uniroma2/marchidori/maininterface/" + fxml));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        currentStage.setScene(newScene);
    }
}
