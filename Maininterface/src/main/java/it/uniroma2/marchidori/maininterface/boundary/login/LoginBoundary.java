package it.uniroma2.marchidori.maininterface.boundary.login;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.LoginController;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
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
    private LoginController loginController;//reference al controller relativo

    // Valore di default per il guest
    private static final String GUEST_EMAIL = "guest@example.com";



    //Metodo UNICO che gestisce i cambi di scena e la logica dei pulsanti

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
                    User authenticatedUser = loginController.login(userEmail, userPassword);
                    if (authenticatedUser != null) {
                        currentUser = Converter.convert(authenticatedUser);
                        SceneSwitcher.changeScene(currentStage,SceneNames.HOME,currentUser);
                    } else {
                        wrongLogin.setText("Wrong email or password!");
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
                    Session.getInstance().setCurrentUser(Converter.userBeanToEntity(currentUser));
                    loginController.caseGuest();
                    SceneSwitcher.changeScene(currentStage,SceneNames.HOME,currentUser);
                }
                // Caso CREATE_ACCOUNT
                case "CREATE_ACCOUNT" -> {
                    if (currentUser == null) {
                        currentUser = new UserBean(
                                "temp@example.com",
                                new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        );
                    }
                    SceneSwitcher.changeScene(currentStage, SceneNames.REGISTER, currentUser);
                }
                default -> logger.log(Level.INFO,"default");
            }
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene.", e);
        }
    }

    //funzioni di realizzazione delle interfaccie useraware e controlleraware
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.loginController = (LoginController) logicController;
    }
}