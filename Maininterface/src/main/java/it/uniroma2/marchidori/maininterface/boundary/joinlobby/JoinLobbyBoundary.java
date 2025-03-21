package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.boundary.ConfirmationPopup;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class JoinLobbyBoundary implements UserAwareInterface, ControllerAwareInterface {

    @FXML
    protected AnchorPane joinLobbyPane;

    // ComboBox per i filtri (type, durata)
    @FXML
    protected ComboBox<String> comboBox1; // "Online"/"Presenza"
    @FXML
    protected ComboBox<String> comboBox2; // "Singola"/"Campagna"

    @FXML
    protected ImageView joinLobbyImage;
    @FXML
    protected ImageView manageLobbyImage;

    @FXML
    private Button consultRules;
    @FXML
    private Button joinlobby;
    @FXML
    private Button manageLobby;
    @FXML
    private Button myChar;
    @FXML
    private Button userButton;

    // TableView e colonne
    @FXML
    protected TableView<LobbyBean> lobbyTableView;
    @FXML
    protected TableColumn<LobbyBean, String> lobbyNameColumn;
    @FXML
    protected TableColumn<LobbyBean, String> numberOfPlayersColumn;
    @FXML
    protected TableColumn<LobbyBean, Button> joinButtonColumn;
    @FXML
    protected TableColumn<LobbyBean, Button> favouriteButton;
    @FXML
    protected TableColumn<LobbyBean, String> durationColumn;
    @FXML
    protected TableColumn<LobbyBean, String> liveOnlineColumn;

    @FXML
    protected TextField searchBar;

    @FXML
    protected Button resetButton;

    protected UserBean currentUser;
    protected User currentEntity = Session.getInstance().getCurrentUser();

    // reference al controller relativo
    protected JoinLobbyController controller;

    // Controller del popup di conferma con timer
    protected ConfirmationPopup confirmationPopup;

    // Observable list delle lobby filtrate
    private ObservableList<LobbyBean> filteredLobbies;

    //funzione di inizializzazzione della GUI
    @FXML
    public void initialize() throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("FXML confirmationPopup.fxml non trovato!");
            }
            AnchorPane popupRoot = loader.load();
            joinLobbyPane.getChildren().add(popupRoot);
            confirmationPopup = loader.getController();
            if (confirmationPopup == null) {
                throw new NullPointerException("Errore: il controller di confirmationPopup.fxml è null!");
            }
        } catch (IOException e) {
            throw new SceneChangeException("errore nel caricamento del popup controller", e);
        }

        // Popola i ComboBox
        comboBox1.setItems(FXCollections.observableArrayList("Online", "Live"));
        comboBox2.setItems(FXCollections.observableArrayList("One-Shot", "Campaign"));

        //barra di ricerca
        searchBar.textProperty().addListener((observable, oldValue, newValue) ->
            doFilter()
        );

        // Listener per i filtri
        comboBox1.valueProperty().addListener((obs, oldVal, newVal) ->
            doFilter()
        );
        comboBox2.valueProperty().addListener((obs, oldVal, newVal) ->
            doFilter()
        );

        // Carica le lobby iniziali
        List<LobbyBean> initial = Converter.convertLobbyListEntityToBean(controller.getLobbies());
        filteredLobbies = FXCollections.observableArrayList(initial);

        // Imposta le colonne della tableview
        lobbyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberOfPlayersColumn.setCellValueFactory(new PropertyValueFactory<>("players"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        liveOnlineColumn.setCellValueFactory(new PropertyValueFactory<>("liveOnline"));

        numberOfPlayersColumn.setCellValueFactory(new PropertyValueFactory<>("playersCountDisplay"));
        lobbyTableView.setItems(filteredLobbies);
        lobbyTableView.refresh();
        refreshTable();
    }



    //funzione refresh della tableview
    public void refreshTable() {
        if (controller != null) {

            List<Lobby> rawLobbies = controller.getLobbies();
            List<LobbyBean> updatedList = Converter.convertLobbyListEntityToBean(rawLobbies);

            filteredLobbies.setAll(updatedList);
            lobbyTableView.setItems(filteredLobbies); // Forza il reset della lista
            lobbyTableView.refresh(); // Forza l'aggiornamento visivo
        }
    }

    //applicazione dei filtri e delego al controller il filtraggio ed il ritorno della lista filtrata
    public void doFilter() {
        String type = comboBox1.getValue();
        String duration = comboBox2.getValue();
        String searchQuery= searchBar.getText().toLowerCase();
        List<LobbyBean> result = controller.filterLobbies(type, duration,searchQuery);
        filteredLobbies.setAll(result);
    }


    //funzione reset filtri
    @FXML
    public void resetFilters(ActionEvent event) {
        comboBox1.setValue(null);
        comboBox2.setValue(null);
        doFilter();
    }

    //funzione di navigazione tra le scene
    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();

        // Esegui il cambio scena
        Stage currentStage = (Stage) joinLobbyPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene.", e);
        }
    }

    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) joinLobbyPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

    //funzioni di realizzazione delle interfaccie useraware e controlleraware

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (JoinLobbyController) logicController;
    }
}
