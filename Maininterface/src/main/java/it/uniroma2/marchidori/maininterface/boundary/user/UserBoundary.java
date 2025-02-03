package it.uniroma2.marchidori.maininterface.boundary.user;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class UserBoundary implements UserAwareInterface {

    @FXML
    protected Button consultRules;

    @FXML
    protected Button joinLobby;

    @FXML
    protected Button manageLobby;

    @FXML
    protected Button myChar;

    @FXML
    protected VBox vBox;

    @FXML
    protected TextField emailUser;

    @FXML
    protected Button goToHome;

    @FXML
    protected Button logOutButton;

    @FXML
    protected TextField roleUser;

    @FXML
    protected Button userButton;

    @FXML
    protected TextField userName;

    @FXML
    protected AnchorPane userPane;

    @FXML
    protected Button switchRoleButton;

    // --> Usiamo SOLO qui la variabile currentUser: le sottoclassi la ereditano
    // Meglio "protected" se le sottoclassi devono accedervi direttamente
    protected UserBean currentUser;

    /**
     * Invocato automaticamente da JavaFX dopo l'iniezione dei nodi @FXML.
     */
    @FXML
    protected void initialize() {
        configureUI();
    }

    /**
     * Configurazione di base. Le sottoclassi la possono sovrascrivere.
     */
    protected void configureUI() {
        if (currentUser != null) {
            userName.setText(currentUser.getUsername());
            roleUser.setText(currentUser.getRoleBehavior().getRoleName());
            System.out.println(currentUser.getRoleBehavior().getRoleName());
            emailUser.setText(currentUser.getEmail());
        }
    }

    /**
     * Metodo per passare l'oggetto User da fuori (es. quando si carica questo controller).
     */

    // ------------------------------------------------------------
    // Metodi generici di cambio scena
    // ------------------------------------------------------------


    @FXML
    protected void onClickGoToConsultRules(ActionEvent event) {
        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to consult rules.", e);
        }
    }

    @FXML
    protected void onClickGoToHome(ActionEvent event) {
        try {
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to home.", e);
        }
    }

    @FXML
    protected void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to join lobby.", e);
        }
    }

    @FXML
    protected void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to manage lobby list.", e);
        }
    }

    @FXML
    protected void onClickLogOut(ActionEvent event) {
        try {
            changeScene(SceneNames.LOGIN);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to login.", e);
        }
    }

    @FXML
    protected void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to my character list.", e);
        }
    }

    @FXML
    protected void onClickUser(ActionEvent event) {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to user.", e);
        }
    }

    @FXML
    protected void onClickSwitchRole(ActionEvent event) throws IOException {
        // Vuoto: verrà override in UserPlayerBoundary e UserDMBoundary
    }

    @FXML
    protected void changeScene(String fxml) throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) userPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
