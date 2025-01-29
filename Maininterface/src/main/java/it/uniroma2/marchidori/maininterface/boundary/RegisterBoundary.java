package it.uniroma2.marchidori.maininterface.boundary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class RegisterBoundary {

    @FXML
    private Button Register;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private AnchorPane registerPane;

    @FXML
    private Label wrongLogin;

    @FXML
    void clickRegister(ActionEvent event) {

    }

}
