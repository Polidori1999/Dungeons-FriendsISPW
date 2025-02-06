package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
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

    // ComboBox per i filtri (type, durata, numPlayers)
    @FXML
    protected ComboBox<String> comboBox1; // "Online"/"Presenza"
    @FXML
    protected ComboBox<String> comboBox2; // "Singola"/"Campagna"
    @FXML
    protected ComboBox<String> comboBox3; // "2", "3", ... "8"
    @FXML
    protected ComboBox<String> comboBox4;

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
    protected TableColumn<LobbyBean, Void> joinButtonColumn;
    @FXML
    protected TableColumn<LobbyBean, Void> favouriteButton;

    @FXML
    protected Button resetButton;

    protected UserBean currentUser;
    protected User currentEntity = Session.getCurrentUser();

    // Controller per logica di join
    protected JoinLobbyController controller;

    // Controller del popup di conferma con timer
    protected ConfirmationPopupController confirmationPopupController;

    // Observable list delle lobby filtrate
    private ObservableList<LobbyBean> filteredLobbies;

    @FXML
    public void initialize() {
        // Inizializza il JoinLobbyController e il popup di conferma
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("FXML confirmationPopup.fxml non trovato!");
            }
            AnchorPane popupRoot = loader.load();
            joinLobbyPane.getChildren().add(popupRoot);
            confirmationPopupController = loader.getController();
            if (confirmationPopupController == null) {
                throw new NullPointerException("Errore: il controller di confirmationPopup.fxml è null!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Popola i ComboBox
        comboBox1.setItems(FXCollections.observableArrayList("Online", "Presenza"));
        comboBox2.setItems(FXCollections.observableArrayList("Singola", "Campagna"));
        comboBox3.setItems(FXCollections.observableArrayList("2", "3", "4", "5", "6", "7", "8"));

        // Listener per i filtri
        comboBox1.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        comboBox2.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        comboBox3.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());

        // Carica le lobby iniziali
        List<LobbyBean> initial = controller.getList(LobbyRepository.getAllLobbies());
        filteredLobbies = FXCollections.observableArrayList(initial);

        // Imposta le colonne: usa una lambda per la colonna "players"
        lobbyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberOfPlayersColumn.setCellValueFactory(new PropertyValueFactory<>("players"));

        lobbyTableView.setItems(filteredLobbies);
        refreshTable();
    }



    public void refreshTable() {
        if (currentUser != null) {
            filteredLobbies.clear();
            List<LobbyBean> updatedList = controller.getList(LobbyRepository.getAllLobbies()); // Prendi i dati aggiornati
            filteredLobbies.addAll(updatedList); // Riaggiungi i dati aggiornati
            lobbyTableView.refresh();
        }
    }

    private void doFilter() {
        String type = comboBox1.getValue();
        String duration = comboBox2.getValue();
        String numPlayers = comboBox3.getValue();
        List<LobbyBean> result = controller.filterLobbies(type, duration, numPlayers);
        filteredLobbies.setAll(result);
    }

    @FXML
    public void resetFilters(ActionEvent event) {
        comboBox1.setValue(null);
        comboBox2.setValue(null);
        comboBox3.setValue(null);
        doFilter();
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from join lobby to consult rules.", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from join lobby to home.", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from join lobby to join lobby.", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from join lobby to manage lobby list.", e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from join lobby to user.", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from join lobby to character list.", e);
        }
    }

    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) joinLobbyPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (JoinLobbyController) logicController;
    }
}
