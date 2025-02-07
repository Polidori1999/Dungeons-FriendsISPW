package it.uniroma2.marchidori.maininterface.boundary.login;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.LoginController;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.entity.Session;
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
import java.util.UUID;
import java.util.logging.Level;

import java.util.logging.Logger;


public class LoginBoundary implements UserAwareInterface {
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

    private UserDAOFileSys authentication;
    private UserBean currentUser;
    //private LoginController loginController;

    /*public LoginBoundary() {
        this.authentication = new UserDAOFileSys();
    }*/
    private LoginController loginController = new LoginController();
    @FXML
    public void initialize() {
        login.maxWidthProperty().bind(anchorLoginPane.widthProperty().divide(2));
        login.maxHeightProperty().bind(anchorLoginPane.heightProperty().divide(2));
    }

    @FXML
    void clickLogin(ActionEvent event) throws IOException {
        String userEmail = email.getText();
        String userPassword = password.getText();
        try {
            UserBean authenticatedUser = loginController.login(userEmail, userPassword);
            if (authenticatedUser != null) {
                currentUser=authenticatedUser;
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
            currentUser = new UserBean("guest", "guest@example.com","guest" ,RoleEnum.GUEST, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            logger.info(">>> Utente impostato come Guest. Ruolo: " + currentUser.getRoleBehavior());

            Session.setCurrentUser(Converter.userBeanToEntity(currentUser));
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from login to home.", e);
        }
    }

    @FXML
    void onClickCreate(ActionEvent event) {
        try {
            if (currentUser == null) {
                logger.info(">>> Creazione di un UserBean temporaneo per la registrazione.");
                currentUser = new UserBean(UUID.randomUUID().toString(), "temp@example.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            }
            logger.info(">>> LoginBoundary: Sto passando currentUser con ruolo: " + currentUser.getRoleBehavior());
            changeScene(SceneNames.REGISTER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from login to register.", e);
        }
    }



    @FXML
    private void changeScene(String fxml) throws IOException {
        if (currentUser == null) {
            logger.info(">>> ERRORE: currentUser è NULL! Creazione di un UserBean di fallback.");
            currentUser = new UserBean("guest", "guest@example.com","guest",RoleEnum.GUEST, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
        logger.log(Level.FINE, "Cambiando scena: {0} con UserBean ruolo: {1}", new Object[]{fxml, currentUser.getRoleBehavior()});
        Stage currentStage = (Stage) anchorLoginPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }
    /*@FXML
    private void changeScene(String fxml, UserBean user) throws IOException {
        if (user == null) {
            logger.info(">>> ERRORE: user è NULL! Creazione di un UserBean di fallback.");
            user = new UserBean("guest", "Guest", "guest@example.com", RoleEnum.GUEST, new ArrayList<>(), new ArrayList<>());
        }

        logger.log(Level.FINE, "Cambiando scena: {0} con UserBean ruolo: {1}", new Object[]{fxml, user.getRoleBehavior()});
        Stage currentStage = (Stage) anchorLoginPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, user);
    }*/

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
