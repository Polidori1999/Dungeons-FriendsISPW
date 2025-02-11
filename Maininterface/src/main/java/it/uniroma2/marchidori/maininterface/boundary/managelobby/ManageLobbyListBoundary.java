package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.LobbyChangeListener;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.control.UserService;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;


import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;

public class ManageLobbyListBoundary implements UserAwareInterface, ControllerAwareInterface, LobbyChangeListener {

    @FXML
    protected AnchorPane manageLobbyListPane;

    // Tutti i pulsanti (nessun onAction individuale)
    @FXML
    protected Button consultRules;
    @FXML
    protected Button joinlobby;
    @FXML
    protected Button manageLobby;
    @FXML
    protected Button myChar;
    @FXML
    protected Button goToHome;
    @FXML
    protected Button newLobbyButton;
    @FXML
    protected Button userButton;

    @FXML
    protected VBox vBox;

    // TableView e relative colonne
    @FXML
    protected TableView<LobbyBean> tableViewLobby;
    @FXML
    protected TableColumn<LobbyBean, String> tableViewLobbyName;
    @FXML
    protected TableColumn<LobbyBean, String> tableViewMaxPlayers;
    @FXML
    protected TableColumn<LobbyBean, String> tableViewDuration;
    @FXML
    protected TableColumn<LobbyBean, String> tableViewLiveOrNot;
    @FXML
    protected TableColumn<LobbyBean, Button> tableViewLobbyDelete;
    @FXML
    protected TableColumn<LobbyBean, Button> tableViewLobbyEdit;

    protected UserBean currentUser;
    protected ManageLobbyListController controller;
    protected ObservableList<LobbyBean> data = FXCollections.observableArrayList();
    private LobbyBean pendingDeleteBean;
    protected ConfirmationPopupController confirmationPopupController;


    @FXML
    protected void initialize() {
        if (currentUser == null) {
            logger.severe(">>> ERRORE: currentUser è null in ManageLobbyListBoundary.");
            return;
        }
        if (currentUser.getJoinedLobbies() == null) {
            currentUser.setJoinedLobbies(new ArrayList<>());
        }
        confirmationPopupController = ConfirmationPopupController.loadPopup(manageLobbyListPane);

        data.clear();
        data.addAll(controller.getJoinedLobbies());
        //se usiakmo observable la atble view si aggiorna auto ma possiamo anche fare refresh manuake
        tableViewLobbyName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewMaxPlayers.setCellValueFactory(new PropertyValueFactory<>("numberOfPlayers"));
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tableViewLiveOrNot.setCellValueFactory(new PropertyValueFactory<>("liveOnline"));
        // Configura la colonna "Delete"
        TableColumnUtils.setupButtonColumn(tableViewLobbyDelete, "Leave", lobby -> {
            pendingDeleteBean = lobby;
            showLeaveConfirmation();
        });

        tableViewLobby.setItems(data);
    }

    private void showLeaveConfirmation() {
        if (confirmationPopupController != null && pendingDeleteBean != null) {
            String message = "Vuoi eliminare la lobby '" + pendingDeleteBean.getName() + "'?";
            confirmationPopupController.show(
                    message,
                    10,                // timer di scadenza popup
                    this::onConfirmLeave,
                    this::onCancel
            );
        } else {
            logger.severe("Errore: ConfirmationPopupController non inizializzato o pendingDeleteBean è null");
        }
    }


    private void onConfirmLeave() {
        if (pendingDeleteBean != null) {
            String lobbyName = pendingDeleteBean.getName();

            // Rimuovi dalla TableView
            tableViewLobby.getItems().remove(pendingDeleteBean);

            // Chiedi al controller di rimuoverla (la logica differenziata in base al ruolo viene gestita all'interno del controller)
            controller.deleteLobby(lobbyName);

            // Rimuovi la chiamata seguente, poiché il controller già gestisce la rimozione dalla repository in caso di proprietario
            // LobbyRepository.removeLobby(lobbyName);

            pendingDeleteBean = null;
        }
    }



    private void onCancel() {
        pendingDeleteBean = null;
    }


    /**
     * Unico metodo per gestire la navigazione. Ricava l'FXML dal pulsante premuto (usando userData).
     */
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
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            // Se preferisci, potresti usare un messaggio più "dinamico", come:
            // "Error during change scene from ManageLobbyListBoundary to " + fxml
            throw new SceneChangeException("Error during change scene.", e);
        }
    }



    @Override
    public void onLobbyListChanged() {
        System.out.println("✅ Evento ricevuto: la lista delle lobby è cambiata! Aggiorno la UI...");
        refreshTable();
    }

    public void refreshTable() {
        Platform.runLater(()->{;
            System.out.println("🔄 Aggiornamento UI della lista lobby...");
            tableViewLobby.getItems().clear();
            tableViewLobby.getItems().addAll(controller.getJoinedLobbies());
            tableViewLobby.refresh();  // Se usi una TableView, assicurati che venga rinfrescata!
        });
    }



    public void refreshLobbyList(){
        if(controller==null||currentUser==null){
            return;
        }
        data.clear();
        data.addAll(controller.getJoinedLobbies());
        tableViewLobby.setItems(data);
        tableViewLobby.refresh();
    }

    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

    /* ================ Metodi di iniezione dipendenze ================ */

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ManageLobbyListController) logicController;
    }

}