package it.uniroma2.marchidori.maininterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane anchorLoginPane;

    @FXML
    private Button create_Account;

    @FXML
    private TextField email;

    @FXML
    private Button forgot_password;

    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    @FXML
    private CheckBox remember_me;

    @FXML
    private Label wrongLogin;

    @FXML
    public void initialize() {
        // Imposta la grandezza massima del pulsante a metà delle dimensioni del contenitore
        login.maxWidthProperty().bind(anchorLoginPane.widthProperty().divide(2));
        login.maxHeightProperty().bind(anchorLoginPane.heightProperty().divide(2));
    }

    @FXML
    void clickLogin(ActionEvent event) throws IOException {
        login.maxWidthProperty().bind(anchorLoginPane.widthProperty().divide(2));
        login.maxHeightProperty().bind(anchorLoginPane.heightProperty().divide(2));
        checkLogin();
    }

    private void checkLogin() throws IOException {
        if(email.getText().equals("Mario") && password.getText().equals("123")){
            wrongLogin.setText("success!");
            goToHome();

        }else if(email.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogin.setText("please enter your data!");
        }else{
            wrongLogin.setText("wrong email or password!");
        }
    }

    @FXML
    private void goToHome() throws IOException {
        // Carica il file FXML della seconda scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();

        // Ottieni lo stage attuale
        Stage stage = (Stage) anchorLoginPane.getScene().getWindow(); // Alternativa: (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
