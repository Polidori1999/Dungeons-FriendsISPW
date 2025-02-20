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

    private RegisterController controller;//reference al controller relativo

    public RegisterBoundary() {
        //empty builder
    }

    //gestione dell'azione click sul bottone register
    @FXML
    void clickRegister(ActionEvent event) {
        String emailText = email.getText();
        String passwordText = password.getText();
        String confirmPasswordText = confirmPassword.getText();
        if (emailText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Campi vuoti, registrazione non possibile!");
            }
            wrongLogin.setText("Riempi tutti i campi!");
            return;
        }
        if (!passwordText.equals(confirmPasswordText)) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Le password non coincidono!");
            }
            wrongLogin.setText("Le password non coincidono!");
            return;
        }
        registerMethod(emailText, passwordText);
    }

    //delego al controller l 'operazione di register dello user
    public void registerMethod(String emailText, String passwordText) {
        boolean success = controller.register(emailText, passwordText);
        if (success) {
            // Registrazione riuscita
            try {
                changeScene();
            } catch (IOException e) {
                logger.warning("Errore nel cambio scena: " + e.getMessage());
                wrongLogin.setText("Errore interno, riprova più tardi.");
            }
        } else {
            wrongLogin.setText("Account already exists!");
        }
    }

    //gestione di ritorno alla schermata di login
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
        SceneSwitcher.changeScene(currentStage, SceneNames.LOGIN, null);  // Cambia scena con SceneSwitcher
    }

    //funzioni di realizzazione delle interfaccie useraware e controlleraware
    @Override
    public void setCurrentUser(UserBean user) {
        //implementa usereawareinterface
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (RegisterController) logicController;
    }
}
