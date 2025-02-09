package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;

import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;

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
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();


        // Se hai logiche speciali, ad esempio sul pulsante "New Lobby" (manageLobby.fxml):
        if (SceneNames.MANAGE_LOBBY.equals(fxml)) {
            currentUser.setSelectedLobbyName(null);
            logger.info("Reset selectedLobbyName. Current user: " + currentUser.getEmail());
        }

        // Esegui il cambio scena
        Stage currentStage = (Stage) homePane.getScene().getWindow();
        try {

            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            // Se preferisci, potresti usare un messaggio più "dinamico", come:
            // "Error during change scene from ManageLobbyListBoundary to " + fxml
            throw new SceneChangeException("Error during change scene.", e);
        }
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

}
