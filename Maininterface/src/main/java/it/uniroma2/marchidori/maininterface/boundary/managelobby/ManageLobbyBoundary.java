package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
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

public class ManageLobbyBoundary implements UserAwareInterface, ControllerAwareInterface {


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
            currentUser.setSelectedLobbyName(null);
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
            currentUser.setSelectedLobbyName(null);
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
            currentUser.setSelectedLobbyName(null);
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
            currentUser.setSelectedLobbyName(null);
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
            currentUser.setSelectedLobbyName(null);
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

    // Metodo per cancellare eventuali filtri (se esiste un pulsante dedicato)
    @FXML
    public void clearFilters(ActionEvent event) {
        durationBox.setValue(null);
        maxPlayersBox.setValue(null);
        liveOnlineBox.setValue(null);
        doFilter();
    }

    // Metodo per applicare i filtri (a seconda dei valori selezionati nelle ComboBox)
    private void doFilter() {
        // Se necessario, implementa la logica per filtrare le lobby basata sui valori delle ComboBox.
        // Per questa classe potrebbe non essere strettamente necessario, a meno che tu non stia usando
        // questi filtri per popolare l'interfaccia.
    }

    // -------------------------------------------------------------
    //                    VARIABILI DI STATO
    // -------------------------------------------------------------
    public UserBean currentUser;
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
        System.out.println("User in ManageLobbyBoundary: " + (currentUser != null ? currentUser.getEmail() : "null"));

        // Determina la modalità in base al campo selectedLobbyName del currentUser
        String selected = currentUser != null ? currentUser.getSelectedLobbyName() : null;
        if (selected == null || selected.isEmpty()) {
            creationMode = true;
            currentBean = new LobbyBean();
            oldName = null;
        } else {
            creationMode = false;
            currentBean = findLobbyByName(selected);
            oldName = selected;
            if (currentBean == null) {
                System.err.println(">>> Non ho trovato la lobby con nome: " + selected);
                creationMode = true;
                currentBean = new LobbyBean();
            }
        }
        if (creationMode) {
            clearFields();
            System.out.println(">>> Modalità creazione attiva.");
        } else {
            populateFields(currentBean);
            System.out.println(">>> Modalità modifica attiva per lobby: " + currentBean.getName());
        }
    }

    private LobbyBean findLobbyByName(String lobbyName) {
        if (currentUser.getJoinedLobbies() == null) {
            return null;
        }
        LobbyBean foundLobby = currentUser.getJoinedLobbies().stream()
                .filter(l -> l.getName().equals(lobbyName))
                .findFirst()
                .orElse(null);
        if (foundLobby == null) {
            return null;
        }
        return new LobbyBean(
                foundLobby.getDuration(),
                foundLobby.getName(),
                foundLobby.getType(),
                foundLobby.getNumberOfPlayers(),
                foundLobby.isOwned()
        );
    }

    // -------------------------------------------------------------
    //                    LOGICA DI SALVATAGGIO
    // -------------------------------------------------------------
    @FXML
    void onClickSaveLobby(ActionEvent event) {
        if (currentBean == null) {
            System.err.println(">>> ERRORE: currentBean è NULL! Non posso salvare.");
            return;
        }

        // Legge il nome dalla TextField e gli altri valori dalle ComboBox
        currentBean.setName(lobbyName.getText());
        currentBean.setType(liveOnlineBox.getValue()); // Valore della ComboBox per il tipo (Live/Online)
        currentBean.setNumberOfPlayers(parseIntOrZero(maxPlayersBox.getValue())); // Numero massimo come stringa convertita in int
        currentBean.setDuration(durationBox.getValue()); // Durata

        if (!creationMode) {
            controller.updateLobby(oldName, currentBean);
        } else {
            controller.createLobby(currentBean);
        }

        // Dopo il salvataggio, resetta la selezione e torna alla lista delle lobby
        currentUser.setSelectedLobbyName(null);
        try {
            SceneSwitcher.changeScene(
                    (Stage) ((Button) event.getSource()).getScene().getWindow(),
                    SceneNames.MANAGE_LOBBY_LIST,
                    currentUser
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    //          Metodi di supporto per i campi della GUI
    // -------------------------------------------------------------
    private void clearFields() {
        lobbyName.setText("");
        // Reset delle ComboBox
        durationBox.setValue(null);
        liveOnlineBox.setValue(null);
        maxPlayersBox.setValue(null);
    }

    private void populateFields(LobbyBean bean) {
        lobbyName.setText(bean.getName());
        liveOnlineBox.setValue(bean.getType());
        maxPlayersBox.setValue(String.valueOf(bean.getNumberOfPlayers()));
        durationBox.setValue(bean.getDuration());
    }

    private int parseIntOrZero(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.err.println(">>> ERRORE: Il valore inserito non è un numero valido: " + input);
            return 0;
        }
    }
}
