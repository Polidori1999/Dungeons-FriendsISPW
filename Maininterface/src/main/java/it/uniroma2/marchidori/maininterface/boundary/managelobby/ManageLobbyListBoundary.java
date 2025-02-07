package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;


public class ManageLobbyListBoundary implements UserAwareInterface, ControllerAwareInterface {

    @FXML
    protected AnchorPane manageLobbyListPane;

    @FXML
    protected Button consultRules;

    @FXML
    protected Button joinlobby;

    @FXML
    protected Button manageLobby;

    @FXML
    protected Button myChar;

    @FXML
    protected VBox vBox;

    @FXML
    protected Button goToHome;

    @FXML
    protected Button newLobbyButton;

    @FXML
    protected TableColumn<LobbyBean, String> tableViewDuration;

    @FXML
    protected TableColumn<LobbyBean, String> tableViewLiveOrNot;

    @FXML
    protected TableView<LobbyBean> tableViewLobby;

    @FXML
    protected TableColumn<LobbyBean, Button> tableViewLobbyDelete;

    @FXML
    protected TableColumn<LobbyBean, Button> tableViewLobbyEdit;

    @FXML
    protected TableColumn<LobbyBean, String> tableViewLobbyName;

    @FXML
    protected TableColumn<LobbyBean, String> tableViewMaxPlayers;

    @FXML
    protected Button userButton;

    // Variabile per gestire l'utente corrente
    protected UserBean currentUser;
    protected ManageLobbyListController controller;
    protected ObservableList<LobbyBean> data = FXCollections.observableArrayList();

    /**
     * Invocato automaticamente da JavaFX dopo l'iniezione dei nodi @FXML.
     */
    @FXML
    protected void initialize() {
        if (currentUser == null) {
            logger.severe(">>> ERRORE: currentUser è null in ManageLobbyListBoundary.");
            return;  // Interrompi l'esecuzione se currentUser è null
        }

        if (currentUser.getJoinedLobbies() == null) {
            currentUser.setJoinedLobbies(new ArrayList<>());
        }
        data.clear();
        data.addAll(controller.getJoinedLobbies());
        // Imposta il PropertyValueFactory per le colonne basate sulle proprietà di LobbyBean.
        tableViewLobbyName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewMaxPlayers.setCellValueFactory(new PropertyValueFactory<>("numberOfPlayers"));
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tableViewLiveOrNot.setCellValueFactory(new PropertyValueFactory<>("type"));




        tableViewLobby.setItems(data);
    }


    /**
     * Metodo che inizializza la TableView e configura le colonne.
     */

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
            throw new SceneChangeException("Error during change scene from manage lobby list to join lobby.", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to manage lobby.", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to character list.", e);
        }
    }


    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to user.", e);
        }
    }
    @FXML
    void onClickNewLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a newLobby.fxml", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ManageLobbyListController) logicController;
    }

}
