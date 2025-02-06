package it.uniroma2.marchidori.maininterface.boundary.login;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.RegisterController;
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
import java.util.ArrayList;
import java.util.UUID;

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

    private UserBean currentUser;
    private RegisterController controller;

    public RegisterBoundary() {
        logger.info(">>> RegisterBoundary creato");
    }

    @FXML
    void clickRegister(ActionEvent event) throws IOException {
        try {
            /*if (currentUser == null) {
                // Se currentUser è NULL, creiamo un utente temporaneo
                logger.info(">>> ERRORE: currentUser è NULL! Creazione di un UserBean di fallback.");
                currentUser = new UserBean(UUID.randomUUID().toString(), "TempUser", "temp@example.com",
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            }*/
            String emailText = email.getText();
            String passwordText = password.getText();
            String confirmPasswordText = confirmPassword.getText();
            if (emailText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                logger.warning("⚠️ Campi vuoti, registrazione non possibile!");
                wrongLogin.setText("Riempi tutti i campi!");
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                logger.warning("⚠️ Le password non coincidono!");
                wrongLogin.setText("Le password non coincidono!");
                return;
            }
            logger.info("✅ Tentativo di registrazione con email: " + emailText);

            controller.register(emailText, passwordText);

            logger.info("registrazione completata: "+emailText);


            changeScene(SceneNames.LOGIN);
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
        this.currentUser = user;
        logger.info(">>> RegisterBoundary: currentUser ricevuto con ruolo: " + user.getRoleBehavior());
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (RegisterController) logicController;
    }
}
