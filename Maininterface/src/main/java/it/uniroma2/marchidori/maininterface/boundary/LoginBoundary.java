package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.control.AuthController;
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

public class LoginBoundary {

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
    public UserBean currentUser = new UserBean("123", "Mario", "@lol@", null);


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
            if (checkLogin()) {// Ottieni lo Stage corrente
                changeScene("home.fxml");
            } else {
                wrongLogin.setText("Wrong email or password!");
            }
        } catch (IOException e) {
            throw new SceneChangeException("Error during scene change from login to home.", e);
        }
    }

    @FXML
    void onClickCreate(ActionEvent event) throws IOException {
        try {
            changeScene("register.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during scene change from login to register.", e);
        }
    }

    private boolean checkLogin() {
        String userEmail = email.getText();
        String userPassword = password.getText();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            wrongLogin.setText("Please enter your data!");
            return false;
        }

        // Delego l'autenticazione al servizio
        boolean authenticated = authentication.authenticate(userEmail, userPassword);

        if (authenticated) {
            wrongLogin.setText("Success!");
        } else {
            wrongLogin.setText("Wrong email or password!");
        }

        return authenticated;
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) anchorLoginPane.getScene().getWindow();  // Ottieni lo Stage corrente
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Usa SceneSwitcher per cambiare scena
    }

}
