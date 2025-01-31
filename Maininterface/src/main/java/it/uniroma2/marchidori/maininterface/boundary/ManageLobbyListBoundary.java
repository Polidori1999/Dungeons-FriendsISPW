package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.bean.UserBean;  // Importa UserBean
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageLobbyListBoundary {

    @FXML
    private AnchorPane manageLobbyListPane;

    @FXML
    private Button consultRules;

    @FXML
    private Button joinlobby;

    @FXML
    private Button manageLobby;

    @FXML
    private Button myChar;

    @FXML
    private VBox vBox;

    @FXML
    private Button goToHome;

    @FXML
    private Button newLobbyButton;

    @FXML
    private TableColumn<LobbyBean, String> tableViewDuration;

    @FXML
    private TableColumn<LobbyBean, String> tableViewLiveOrNot;

    @FXML
    private TableView<LobbyBean> tableViewLobby;

    @FXML
    private TableColumn<LobbyBean, Void> tableViewLobbyDelete;

    @FXML
    private TableColumn<LobbyBean, Void> tableViewLobbyEdit;

    @FXML
    private TableColumn<LobbyBean, String> tableViewLobbyName;

    @FXML
    private TableColumn<LobbyBean, String> tableViewMaxPlayers;

    @FXML
    private Button userButton;

    private UserBean currentUser;  // Variabile per gestire l'utente corrente

    @FXML
    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;  // Metodo per settare currentUser
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
        try {
            changeScene("consultRules.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to consult rules.", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene("home.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to home.", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene("joinLobby.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to join lobby.", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene("manageLobbyList.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to manage lobby.", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene("characterList.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to character list.", e);
        }
    }

    @FXML
    void onClickNewLobby(ActionEvent event) {
        try {
            changeScene("manageLobby.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to new lobby.", e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene("user.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to user.", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher passando currentUser
    }

}
