package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.factory.LobbyFactory;
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

    // -------------------------------------------------------------
    //                      VARIABILI FXML
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    //                    VARIABILI DI STATO
    // -------------------------------------------------------------
    private UserBean currentUser;
    private ManageLobbyController controller;

    /** true = creazione nuova lobby, false = modifica di una lobby esistente */
    private boolean creationMode;

    /** Bean della lobby in creazione/modifica */
    protected LobbyBean currentBean;

    /** Vecchio nome della lobby, utile per l’update */
    private String oldName;

    // -------------------------------------------------------------
    //             INIEZIONE UTENTE E CONTROLLER
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    //             METODO DI INIZIALIZZAZIONE
    // -------------------------------------------------------------
    @FXML
    private void initialize() {

        durationBox.setItems(FXCollections.observableArrayList("One-Shot", "Campaign"));
        maxPlayersBox.setItems(FXCollections.observableArrayList("2", "4", "6"));
        liveOnlineBox.setItems(FXCollections.observableArrayList("Live", "Online"));

        LOGGER.log(Level.INFO, "User in ManageLobbyBoundary: {0}",
                (currentUser != null ? currentUser.getEmail() : "null"));

        // Determina la modalità in base al campo selectedLobbyName del currentUser
        String selected = currentUser != null ? currentUser.getSelectedLobbyName() : null;
        LOGGER.log(Level.INFO, "Current user email: {0}", currentUser.getEmail());
        LOGGER.log(Level.INFO, "Selected Lobby Name: {0}", currentUser.getSelectedLobbyName());

        if (selected == null || selected.isEmpty()) {
            creationMode = true;
            currentBean = LobbyFactory.createBean();
            oldName = null;
        } else {
            creationMode = false;
            currentBean = controller.findLobbyByName(selected, currentUser.getJoinedLobbies());
            oldName = selected;
            if (currentBean == null) {
                LOGGER.log(Level.SEVERE, "Non ho trovato la lobby con nome: {0}", selected);
                creationMode = true;
                currentBean = LobbyFactory.createBean();
            }
        }


        if (creationMode) {
            clearFields();
            LOGGER.info("Modalità creazione attiva.");
        } else {
            populateFields(currentBean);
            // Disabilita il campo del nome per impedire la modifica in modalità edit
            lobbyName.setEditable(false);
            lobbyName.setFocusTraversable(false);
            // Disabilita il ComboBox del numero di giocatori per renderlo immutabile
            maxPlayersBox.setDisable(true);
            LOGGER.log(Level.INFO, "Modalità modifica attiva per lobby: {0}", currentBean.getName());
        }
    }

    // -------------------------------------------------------------
    //                    LOGICA DI SALVATAGGIO
    // -------------------------------------------------------------
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

        // Validazione del bean
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

    // -------------------------------------------------------------
    //          Metodi di supporto per i campi della GUI
    // -------------------------------------------------------------
    private void clearFields() {
        lobbyName.setText("");
        infoLink.setText("");
        durationBox.setValue(null);
        liveOnlineBox.setValue(null);
        maxPlayersBox.setValue(null);
    }

    private void populateFields(LobbyBean bean) {
        lobbyName.setText(bean.getName());
        infoLink.setText(bean.getInfoLink());
        liveOnlineBox.setValue(bean.getLiveOnline());
        maxPlayersBox.setValue(String.valueOf(bean.getMaxOfPlayers()));
        durationBox.setValue(bean.getDuration());
    }

    private int parseIntOrZero(String input) {
        // Se input è null o vuoto, restituisci 0
        if (input == null || input.trim().isEmpty()) {
            LOGGER.log(Level.SEVERE, "ERRORE: Il campo per il numero di giocatori è vuoto o nullo.");
            return 0;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "ERRORE: Il valore inserito non è un numero valido: {0}", input);
            return 0;
        }
    }

    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();

        // Esegui il cambio scena
        Stage currentStage = (Stage) manageLobbyPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            // Se preferisci, potresti usare un messaggio più "dinamico", come:
            // "Error during change scene from ManageLobbyListBoundary to " + fxml
            throw new SceneChangeException("Error during change scene.", e);
        }
    }
}