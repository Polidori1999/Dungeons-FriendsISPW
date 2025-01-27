package it.uniroma2.marchidori.maininterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private ComboBox<?> ComboBox1;

    @FXML
    private ComboBox<?> ComboBox2;

    @FXML
    private ComboBox<?> ComboBox3;

    @FXML
    private Button Consult_rules;

    @FXML
    private AnchorPane HomePane;

    @FXML
    private Button Joinlobby;

    @FXML
    private Button ManageLobby;

    @FXML
    private Button Mychar;

    @FXML
    private Button userButton;

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        goToLogin();
    }

@FXML
    private void goToLogin() throws IOException {
        // Carica il file FXML della seconda scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        // Ottieni lo stage attuale
        Stage stage = (Stage) HomePane.getScene().getWindow(); // Alternativa: (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
