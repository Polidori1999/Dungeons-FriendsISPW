package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private AnchorPane manageLobbyPane;

    @FXML
    private Button goToHome;

    @FXML
    private Button userButton;

    @FXML
    private VBox vBox;

    @FXML
    private TextField lobbyDuration;

    @FXML
    private TextField lobbyType;

    @FXML
    private TextField lobbyMaxPlayers;

    @FXML
    private TextField lobbyName;

    @FXML
    private Button goBackToList;

    @FXML
    private Button saveButton;

    @FXML
    private Button myChar;

    // Metodo già presente per il cambio scena verso JoinLobby
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

    // ***** AGGIUNGI QUESTO METODO per risolvere l'errore onClickGoToConsultRules *****
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

    // Se necessario, aggiungi anche un handler per onClickGoToManageLobby (se il file FXML lo richiede)
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
        System.out.println(currentUser.getUsername());
        // Determina la modalità in base al campo selectedLobbyName nel currentUser
        String selected = currentUser.getSelectedLobbyName();
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
        it.uniroma2.marchidori.maininterface.entity.Lobby foundLobby = currentUser.getJoinedLobbies().stream()
                .filter(l -> l.getLobbyName().equals(lobbyName))
                .findFirst()
                .orElse(null);
        if (foundLobby == null) {
            return null;
        }
        return new LobbyBean(
                foundLobby.getDuration(),
                foundLobby.getLobbyName(),
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
        System.out.println(">> onClickSaveLobby() INVOCATO!");
        System.out.println(">>> creationMode attuale: " + creationMode);
        System.out.println(">>> oldName attuale: " + oldName);

        if (currentBean == null) {
            System.err.println(">>> ERRORE: currentBean è NULL! Non posso salvare.");

        }
        currentBean.setName(lobbyName.getText());
        currentBean.setType(lobbyType.getText());
        currentBean.setNumberOfPlayers(parseIntOrZero(lobbyMaxPlayers.getText()));
        currentBean.setDuration(lobbyDuration.getText());

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
    //                 METODI DI NAVIGAZIONE SCENA
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    //          Metodi di supporto per i campi della GUI
    // -------------------------------------------------------------
    private void clearFields() {
        lobbyName.setText("");
        lobbyDuration.setText("");
        lobbyType.setText("");
        lobbyMaxPlayers.setText("");
    }

    private void populateFields(LobbyBean bean) {
        lobbyName.setText(bean.getName());
        lobbyType.setText(bean.getType());
        lobbyMaxPlayers.setText(String.valueOf(bean.getNumberOfPlayers()));
        lobbyDuration.setText(bean.getDuration());
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
