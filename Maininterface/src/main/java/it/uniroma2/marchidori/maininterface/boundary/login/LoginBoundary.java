package it.uniroma2.marchidori.maininterface.boundary.login;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.LoginController;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginBoundary implements UserAwareInterface, ControllerAwareInterface {
    private static final Logger logger = Logger.getLogger(LoginBoundary.class.getName());

    @FXML
    private AnchorPane anchorLoginPane;

    @FXML
    private Button createAccount;   // Pulsante "Create Account"

    @FXML
    private Button forgotPassword;  // Pulsante "Forgot Password"

    @FXML
    private Button login;           // Pulsante "Login"

    @FXML
    private Button guest;           // Pulsante "Guest"

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Label wrongLogin;

    private UserBean currentUser;
    private LoginController loginController;

    // Valore di default per il guest
    private static final String GUEST_EMAIL = "guest@example.com";

    @FXML
    public void initialize() {
        // Binding vari, se necessario
        login.maxWidthProperty().bind(anchorLoginPane.widthProperty().divide(2));
        login.maxHeightProperty().bind(anchorLoginPane.heightProperty().divide(2));
    }

    /**
     * Metodo UNICO che gestisce i cambi di scena e la logica dei pulsanti
     */
    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        // Il valore userData corrisponde alle azioni che vogliamo gestire
        String action = (String) sourceButton.getUserData();
        Stage currentStage = (Stage) anchorLoginPane.getScene().getWindow();


        try {
            switch (action) {

                // Caso LOGIN
                case "LOGIN" -> {
                    String userEmail = email.getText();
                    String userPassword = password.getText();
                    UserBean authenticatedUser = loginController.login(userEmail, userPassword);
                    if (authenticatedUser != null) {
                        currentUser = authenticatedUser;
                        logger.log( Level.INFO,">>> Login avvenuto con successo. Ruolo: {}", currentUser.getRoleBehavior());
                        SceneSwitcher.changeScene(currentStage,SceneNames.HOME,currentUser);
                    } else {
                        wrongLogin.setText("Wrong email or password!");
                        logger.log( Level.INFO,">>> ERRORE: Login fallito. UserBean è NULL!");
                    }
                }

                // Caso GUEST
                case "GUEST" -> {
                    currentUser = new UserBean(
                            GUEST_EMAIL,
                            "guest",
                            RoleEnum.GUEST,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );
                    logger.log(Level.INFO,">>> Utente impostato come Guest. Ruolo: {}", currentUser.getRoleBehavior());

                    // Se usi session, aggiorna l'entity in session
                    Session.getInstance().setCurrentUser(Converter.userBeanToEntity(currentUser));
                    SceneSwitcher.changeScene(currentStage,SceneNames.HOME,currentUser);
                }

                // Caso CREATE_ACCOUNT
                case "CREATE_ACCOUNT" -> {
                    if (currentUser == null) {
                        logger.log(Level.INFO,">>> Creazione di un UserBean temporaneo per la registrazione.");
                        currentUser = new UserBean(
                                "temp@example.com",
                                new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        );
                    }
                    logger.info(">>> Sto passando currentUser con ruolo: " + currentUser.getRoleBehavior());
                    SceneSwitcher.changeScene(currentStage, SceneNames.REGISTER, currentUser);
                }
                default -> logger.log(Level.INFO,"boh");
            }
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene.", e);
        }
    }

    // -------------------------------------------------------------
    //          METODI DI INTERFACCIA (UserAware, ControllerAware)
    // -------------------------------------------------------------
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.loginController = (LoginController) logicController;
    }
}
