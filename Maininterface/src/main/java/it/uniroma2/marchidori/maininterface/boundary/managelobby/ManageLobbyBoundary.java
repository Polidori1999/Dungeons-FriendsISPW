package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.factory.LobbyFactory;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
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

    // Metodi per il cambio scena (es. per JoinLobby, ConsultRules, ecc.)
    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyPane.getScene().getWindow(),
                    SceneNames.JOIN_LOBBY,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena a JOIN_LOBBY.", e);
        }
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyPane.getScene().getWindow(),
                    SceneNames.CONSULT_RULES,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena a CONSULT_RULES.", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyPane.getScene().getWindow(),
                    SceneNames.MANAGE_LOBBY,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena a MANAGE_LOBBY.", e);
        }
    }

    @FXML
    void onClickGoBackToListOfLobbies(ActionEvent event) {
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyPane.getScene().getWindow(),
                    SceneNames.MANAGE_LOBBY_LIST,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena dalla ManageLobby alla lista delle lobby.", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyPane.getScene().getWindow(),
                    SceneNames.HOME,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena dalla ManageLobby alla Home.", e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) {
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyPane.getScene().getWindow(),
                    SceneNames.USER,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena dalla ManageLobby all'User.", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyPane.getScene().getWindow(),
                    SceneNames.CHARACTER_LIST,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena alla Character List.", e);
        }
    }

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
        // Imposta i valori predefiniti delle ComboBox
        durationBox.setItems(FXCollections.observableArrayList("One-Shot", "Campaign"));
        maxPlayersBox.setItems(FXCollections.observableArrayList("2", "4", "8"));
        liveOnlineBox.setItems(FXCollections.observableArrayList("Live", "Online"));

        // Stampa di debug per verificare l'iniezione dell'utente
        LOGGER.log(Level.INFO, "User in ManageLobbyBoundary: {0}", (currentUser != null ? currentUser.getEmail() : "null"));

        // Determina la modalità in base al campo selectedLobbyName del currentUser
        String selected = currentUser != null ? currentUser.getSelectedLobbyName() : null;
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
            LOGGER.log(Level.INFO, "Modalità modifica attiva per lobby: {0}", currentBean.getName());
        }


    }

    // -------------------------------------------------------------
    //                    LOGICA DI SALVATAGGIO
    // -------------------------------------------------------------
    @FXML
    void onClickSaveLobby(ActionEvent event) {
        if (currentBean == null) {
            LOGGER.severe("ERRORE: currentBean è NULL! Non posso salvare.");
            return;
        }

        // Legge il nome dalla TextField e gli altri valori dalle ComboBox
        currentBean.setName(lobbyName.getText());
        currentBean.setLiveOnline(liveOnlineBox.getValue());
        currentBean.setNumberOfPlayers(parseIntOrZero(maxPlayersBox.getValue()));
        currentBean.setDuration(durationBox.getValue());

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
        durationBox.setValue(null);
        liveOnlineBox.setValue(null);
        maxPlayersBox.setValue(null);
    }

    private void populateFields(LobbyBean bean) {
        lobbyName.setText(bean.getName());
        liveOnlineBox.setValue(bean.getLiveOnline());
        maxPlayersBox.setValue(String.valueOf(bean.getNumberOfPlayers()));
        durationBox.setValue(bean.getDuration());
    }

    private int parseIntOrZero(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "ERRORE: Il valore inserito non è un numero valido: {0}", input);
            return 0;
        }
    }
}