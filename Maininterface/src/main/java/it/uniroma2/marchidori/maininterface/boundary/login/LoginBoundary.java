package it.uniroma2.marchidori.maininterface.boundary.login;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.control.AuthController;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginBoundary {
    private static final Logger logger = Logger.getLogger(LoginBoundary.class.getName());


    @FXML
    private AnchorPane anchorLoginPane;

    @FXML
    private Button createAccount;

    @FXML
    private TextField email;

    @FXML
    private Button forgotPassword;

    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Label wrongLogin;

    private AuthController authentication;
    private UserBean currentUser;

    public LoginBoundary() {
        this.authentication = new AuthController();
    }

    @FXML
    public void initialize() {
        login.maxWidthProperty().bind(anchorLoginPane.widthProperty().divide(2));
        login.maxHeightProperty().bind(anchorLoginPane.heightProperty().divide(2));
    }

    @FXML
    void clickLogin(ActionEvent event) throws IOException {
        try {
            UserBean authenticatedUser = checkLogin();
            if (authenticatedUser != null) {
                currentUser = authenticatedUser;
                logger.info(">>> Login avvenuto con successo. Ruolo: " + currentUser.getRoleBehavior());
                changeScene(SceneNames.HOME);
            } else {
                wrongLogin.setText("Wrong email or password!");
                logger.info(">>> ERRORE: Login fallito. UserBean è NULL!");
            }
        } catch (IOException e) {
            throw new SceneChangeException("Error during scene change from login to login.", e);
        }
    }

    @FXML
    void onClickGuest(ActionEvent event) throws IOException {
        try {
            currentUser = new UserBean("guest", "Guest", "guest@example.com", RoleEnum.GUEST, new ArrayList<>(), new ArrayList<>());
            logger.info(">>> Utente impostato come Guest. Ruolo: " + currentUser.getRoleBehavior());

            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from login to home.", e);
        }
    }

    @FXML
    void onClickCreate(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.REGISTER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from login to register.", e);
        }
    }

    private UserBean checkLogin() {
        String userEmail = email.getText();
        String userPassword = password.getText();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            wrongLogin.setText("Please enter your data!");
            return null;
        }

        return authentication.authenticate(userEmail, userPassword);
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        if (currentUser == null) {
            logger.info(">>> ERRORE: currentUser è NULL! Creazione di un UserBean di fallback.");
            currentUser = new UserBean("guest", "Guest", "guest@example.com", RoleEnum.GUEST, new ArrayList<>(), new ArrayList<>());
        }


        logger.log(Level.FINE, "Cambiando scena: {0} con UserBean ruolo: {1}", new Object[]{fxml, currentUser.getRoleBehavior()});
        Stage currentStage = (Stage) anchorLoginPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }
}
