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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private BorderPane borderLoginPane;

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
    void clickLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        if(email.getText().equals("Mario") && password.getText().equals("123")){
            wrongLogin.setText("success!");
            Parent parent = FXMLLoader.load(this.getClass().getResource("Home.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) borderLoginPane.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();

        }else if(email.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogin.setText("please enter your data!");
        }else{
            wrongLogin.setText("wrong email or password!");
        }
    }
}
