package it.uniroma2.marchidori.maininterface.boundary.login;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.RegisterController;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;

import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;

public class RegisterBoundary implements UserAwareInterface, ControllerAwareInterface {

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

    private RegisterController controller;

    public RegisterBoundary() {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(">>> RegisterBoundary creato");
        }
    }

    @FXML
    void clickRegister(ActionEvent event) throws IOException {
        try {
            String emailText = email.getText();
            String passwordText = password.getText();
            String confirmPasswordText = confirmPassword.getText();

            if (emailText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning("⚠️ Campi vuoti, registrazione non possibile!");
                }
                wrongLogin.setText("Riempi tutti i campi!");
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning("⚠️ Le password non coincidono!");
                }
                wrongLogin.setText("Le password non coincidono!");
                return;
            }

            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("✅ Tentativo di registrazione con email: %s", emailText));
            }

            try{
                controller.register(emailText, passwordText);
                logger.info(String.format("Registrazione completata per l'email: %s",emailText));
                changeScene(SceneNames.LOGIN);
            }catch (AccountAlreadyExistsException e){
                logger.warning("Tentativo di registrazione fallito"+e.getMessage());
                wrongLogin.setText("Account already exists!");
            }

            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Registrazione completata per l'email: %s", emailText));
            }

            //changeScene(SceneNames.LOGIN);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from register to login.", e);
        }
    }

    @FXML
    void onClickGoBackToLogin(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.LOGIN);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to login.", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) registerPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, null);  // Cambia scena con SceneSwitcher
    }

    @Override
    public void setCurrentUser(UserBean user) {
        //implementa usereawareinterface
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (RegisterController) logicController;
    }
}
