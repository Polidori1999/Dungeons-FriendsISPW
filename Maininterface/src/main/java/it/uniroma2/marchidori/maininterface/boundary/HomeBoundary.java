package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeBoundary implements UserAwareInterface {

    @FXML
    private AnchorPane homePane;

    @FXML
    private Button consultRules;

    @FXML
    private Button joinLobby;

    @FXML
    private Button manageLobby;

    @FXML
    private Button myChar;

    @FXML
    private Button goToHome;

    @FXML
    private Button userButton;

    private UserBean currentUser;

    @FXML
    void onClickGoToConsultRules(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena verso home.fxml", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena verso joinLobby.fxml", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena verso manageLobbyList.fxml", e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        try {
            System.out.println(currentUser.getRoleBehavior());
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena verso user.fxml", e);
        }
    }

    @FXML
    void onclickGoToMyCharList(ActionEvent event) {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena verso characterList.fxml", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) homePane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
