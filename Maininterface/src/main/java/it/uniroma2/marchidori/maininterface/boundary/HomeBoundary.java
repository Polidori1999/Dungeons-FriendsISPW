package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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

//funzione di nagivazione tra le scene
    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();
        // Esegui il cambio scena
        Stage currentStage = (Stage) homePane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene.", e);
        }
    }

    //realizzazzione userawareinterface
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

}
