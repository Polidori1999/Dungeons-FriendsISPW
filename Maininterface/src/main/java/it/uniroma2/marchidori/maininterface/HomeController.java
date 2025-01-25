package it.uniroma2.marchidori.maininterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController{

    @FXML
    private Button logOut;

    @FXML
    private BorderPane borderHomePane;

    @FXML
    void onClickLogOut(ActionEvent event) throws IOException {
        goToLogin();
    }

    @FXML
    private void goToLogin() throws IOException {
        // Carica il file FXML della seconda scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        // Ottieni lo stage attuale
        Stage stage = (Stage) borderHomePane.getScene().getWindow(); // Alternativa: (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
