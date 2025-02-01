package it.uniroma2.marchidori.maininterface.boundary.login;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterBoundary implements UserAwareInterface {

    @FXML
    private Button register;

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

    private UserBean currentUser;

    @FXML
    void clickRegister(ActionEvent event) throws IOException {
        try {
            changeScene();
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from register to login.", e);
        }
    }

    @FXML
    void onClickGoBackToLogin(ActionEvent event) throws IOException {
        try {
            changeScene();
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to login.", e);
        }
    }


    @FXML
    private void changeScene() throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) registerPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, "login.fxml", currentUser);  // Cambia scena con SceneSwitcher
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
