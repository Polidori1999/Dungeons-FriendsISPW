package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ConfirmationPopup;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
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

public class ManageLobbyListPlayerBoundary implements UserAwareInterface, ControllerAwareInterface {

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
    @FXML
    protected TableColumn<LobbyBean, String> tableViewLobbyInfoLink;

    protected UserBean currentUser;
    protected ManageLobbyListController controller; //reference al controller corrispondente
    protected ObservableList<LobbyBean> data = FXCollections.observableArrayList(); //lista con le lobby filtrate
    private LobbyBean pendingDeleteBean; //reference al bean su ciu compiere operazioni
    protected ConfirmationPopup confirmationPopupController; //reference al controller del confirmation popup


    //funzione di inizializzazione della GUI
    @FXML
    protected void initialize() {
        // Chiama la configurazione di base definita nella superclasse
        if (currentUser == null) {
            logger.severe(">>> ERRORE: currentUser è null in ManageLobbyListBoundary.");
            return;
        }
        if (currentUser.getJoinedLobbies() == null) {
            currentUser.setJoinedLobbies(new ArrayList<>());
        }
        confirmationPopupController = ConfirmationPopup.loadPopup(manageLobbyListPane);

        data.clear();
        data.addAll(controller.getJoinedLobbies());
        //se usiakmo observable la atble view si aggiorna auto ma possiamo anche fare refresh manuake
        tableViewLobbyName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewMaxPlayers.setCellValueFactory(new PropertyValueFactory<>("maxOfPlayers"));
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tableViewLiveOrNot.setCellValueFactory(new PropertyValueFactory<>("liveOnline"));
        tableViewLobbyInfoLink.setCellValueFactory(new PropertyValueFactory<>("infoLink"));
        // Configura la colonna "Delete"
        TableColumnUtils.setupButtonColumn(tableViewLobbyDelete, "Leave", lobby -> {
            pendingDeleteBean = lobby;
            showLeaveConfirmation();
        });

        //riempimento tableview e disabilito il bottone nel caso di PLAYER
        tableViewLobby.setItems(data);
        newLobbyButton.setVisible(false);
        newLobbyButton.setDisable(true);
    }



   //richiesta di conferma con funzioni relative
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
            logger.severe("Errore: ConfirmationPopup non inizializzato o pendingDeleteBean è null");
        }
    }

    //funzione di conferma operazione
    private void onConfirmLeave() {
        if (pendingDeleteBean != null) {
            // Rimuovi dalla TableView
            tableViewLobby.getItems().remove(pendingDeleteBean);

            // Chiedi al controller di rimuoverla (la logica differenziata in base al ruolo viene gestita all'interno del controller)
            controller.leaveLobby(pendingDeleteBean);

            pendingDeleteBean = null;
        }
    }


    //annullamento operazione
    private void onCancel() {
        pendingDeleteBean = null;
    }



    //Unico metodo per gestire la navigazione. Ricava l'FXML dal pulsante premuto (usando userData).
    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();
        // Esegui il cambio scena
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene.", e);
        }
    }

    //delego allo sceneSwitcher
    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }


    //metodi per la realizzazione delle interfacce userawareinterface e controllerawareinterface
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ManageLobbyListController) logicController;
    }

}