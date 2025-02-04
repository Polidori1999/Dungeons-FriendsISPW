package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageLobbyBoundary implements UserAwareInterface, ControllerAwareInterface {

    @FXML
    private Button consultRules;

    @FXML
    private Button joinlobby;

    @FXML
    private Button manageLobby;

    @FXML
    private AnchorPane manageLobbyPane;

    @FXML
    private Button myChar;

    @FXML
    private VBox vBox;

    @FXML
    private TextField charAge;

    @FXML
    private TextField charClass;

    @FXML
    private TextField charLevel;

    @FXML
    private TextField charName;

    @FXML
    private Button goBackToList;

    @FXML
    private Button goToHome;

    @FXML
    private Button saveButton;

    @FXML
    private Button userButton;

    private UserBean currentUser;
    private ManageLobbyController controller;

    @FXML
    void onClickGoBackToListOfLobbies(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to manage lobby list.", e);
        }
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {

        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to consult rules.", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to home.", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to join lobby.", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to manage lobby.", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to login.", e);
        }
    }

    @FXML
    void onClickSaveLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to manage lobby list.", e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to user.", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        // Carica il file FXML della seconda scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/" + fxml));
        Parent root = loader.load();

        // Ottieni lo stage attuale
        Stage stage = (Stage) manageLobbyPane.getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ManageLobbyController) logicController;
    }
}
