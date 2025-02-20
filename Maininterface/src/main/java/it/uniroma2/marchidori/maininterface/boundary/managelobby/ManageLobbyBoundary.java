package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;

import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.Alert;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageLobbyBoundary implements UserAwareInterface, ControllerAwareInterface {

    private static final Logger LOGGER = Logger.getLogger(ManageLobbyBoundary.class.getName());

    @FXML
    protected AnchorPane manageLobbyPane;

    // ComboBox per selezionare gli altri attributi della lobby
    @FXML
    protected ComboBox<String> durationBox;

    @FXML
    protected ComboBox<String> maxPlayersBox;

    @FXML
    protected ComboBox<String> liveOnlineBox;

    // TextField per il nome della lobby
    @FXML
    private TextField lobbyName;
    @FXML
    private TextField infoLink;

    // Pulsanti di navigazione
    @FXML
    private Button goToHome;

    @FXML
    private Button userButton;

    @FXML
    private VBox vBox;

    // Pulsante per salvare la lobby
    @FXML
    private Button saveButton;

    @FXML
    private Button myChar;

    private UserBean currentUser;
    private ManageLobbyController controller;

    // Flag: true se stiamo creando, false se stiamo modificando
    private boolean creationMode;

    //Bean della lobby in creazione/modifica
    protected LobbyBean currentBean;

    //Vecchio nome della lobby, utile per l’update
    private String oldName;

    //funzione di inizializzazzione GUI
    @FXML
    private void initialize() {

        durationBox.setItems(FXCollections.observableArrayList("One-Shot", "Campaign"));
        maxPlayersBox.setItems(FXCollections.observableArrayList("2", "4", "6"));
        liveOnlineBox.setItems(FXCollections.observableArrayList("Live", "Online"));

        // Determina la modalità in base al campo selectedLobbyName del currentUser
        String selected = currentUser != null ? currentUser.getSelectedLobbyName() : null;
        if (currentUser == null) {
            LOGGER.warning("currentUser è null");
        }

        //setting dei parametri
        if (selected == null || selected.isEmpty()) {
            creationMode = true;
            currentBean = new LobbyBean("", "", "", 0, "","", 0);
            oldName = null;
        } else {
            creationMode = false;
            currentBean = controller.findLobbyByName(selected, currentUser.getJoinedLobbies());
            oldName = selected;
            if (currentBean == null) {
                LOGGER.log(Level.SEVERE, "Non ho trovato la lobby con nome: {0}", selected);
                creationMode = true;
                currentBean = new LobbyBean("", "", "", 0, "","", 0);
            }
        }

        if (creationMode) {
            clearFields();
        } else {
            populateFields(currentBean);
            // Disabilita il campo del nome per impedire la modifica in modalità edit
            lobbyName.setEditable(false);
            lobbyName.setFocusTraversable(false);
            // Disabilita il ComboBox del numero di giocatori per renderlo immutabile
            maxPlayersBox.setDisable(true);
        }
    }


    @FXML
    void onClickSaveLobby(ActionEvent event) throws IOException {
        if (currentBean == null) {
            LOGGER.severe("ERRORE: currentBean è NULL! Non posso salvare.");
            return;
        }

        // Legge il nome dalla TextField e gli altri valori dalle ComboBox
        currentBean.setName(lobbyName.getText());
        currentBean.setLiveOnline(liveOnlineBox.getValue());
        currentBean.setMaxOfPlayers(parseIntOrZero(maxPlayersBox.getValue()));
        currentBean.setDuration(durationBox.getValue());
        currentBean.setOwner(currentUser.getEmail());
        currentBean.setInfoLink(infoLink.getText());

        // Validazione del bean delego al controller
        String validationErrors = controller.validate(currentBean);
        if (!validationErrors.isEmpty()) {
            Alert.showError("Errore di Validazione", validationErrors);
            return;
        }

        if (!creationMode) {
            controller.updateLobby(oldName, currentBean);
        } else {
            controller.createLobby(currentBean);
        }

        // Dopo il salvataggio, resetta la selezione e torna alla lista delle lobby
        try {
            SceneSwitcher.changeScene(
                    (Stage) ((Button) event.getSource()).getScene().getWindow(),
                    SceneNames.MANAGE_LOBBY_LIST,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("errore cambio di scena in managelobbylist", e);
        }
    }

    //setta i campi vuoti della scheda della lobby in caso di creazione
    private void clearFields() {
        lobbyName.setText("");
        infoLink.setText("");
        durationBox.setValue(null);
        liveOnlineBox.setValue(null);
        maxPlayersBox.setValue(null);
    }

    //carica dalle info del bean i campi relativi
    private void populateFields(LobbyBean bean) {
        lobbyName.setText(bean.getName());
        infoLink.setText(bean.getInfoLink());
        liveOnlineBox.setValue(bean.getLiveOnline());
        maxPlayersBox.setValue(String.valueOf(bean.getMaxOfPlayers()));
        durationBox.setValue(bean.getDuration());
    }



    private int parseIntOrZero(String input) {
        if (input == null || input.trim().isEmpty()) {
            LOGGER.log(Level.SEVERE, "ERRORE: Il campo per il numero di giocatori è vuoto o nullo.");
            return 0;  // Restituisce 0 in caso di input vuoto o nullo
        }
        try {
            // Prova a convertire l'input in un intero
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            // Se si verifica un errore di formato (input non numerico), logga l'errore
            LOGGER.log(Level.SEVERE, "ERRORE: Il valore inserito non è un numero valido: {0}", input);
            return 0;  // Restituisce 0 in caso di errore di formato
        }
    }


    //funziona di navigazione per i cambi di scena
    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();
        Stage currentStage = (Stage) manageLobbyPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene.", e);
        }
    }

    //realizzazione metodi di interfacce userawareinterface e controllerawareinterface
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
        if (controller != null) {
            controller.setCurrentUser(user);
        }
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ManageLobbyController) logicController;
    }
}