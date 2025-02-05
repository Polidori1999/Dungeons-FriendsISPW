package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.factory.ControllerFactory;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.beans.property.SimpleStringProperty;
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
    protected ComboBox<String> comboBox3; // "2..8"

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
    protected TableColumn<LobbyBean, String> playersColumn;
    @FXML
    protected TableColumn<LobbyBean, Void> joinButtonColumn;
    @FXML
    protected TableColumn<LobbyBean, Void> favouriteButton;

    private UserBean currentUser;
    private static final int MAX_PLAYERS = 8;

    // Controller per logica di join
    private JoinLobbyController controller;

    // Controller del popup di conferma con timer
    private ConfirmationPopupController confirmationPopupController;

    // Observable list delle lobby filtrate
    private ObservableList<LobbyBean> filteredLobbies;


    @FXML
    public void initialize() {
        // Inizializza il JoinLobbyController

        // Carica il file FXML del popup di conferma e aggiungilo al pannello principale
        // Carica il file FXML del popup di conferma e aggiungilo al pannello principale
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
        List<LobbyBean> initial = controller.filterLobbies(null, null, null);
        filteredLobbies = FXCollections.observableArrayList(initial);

        // Inizializza la TableView
        initTableView();
    }

    private void initTableView() {
        lobbyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        playersColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNumberOfPlayers() + "/" + MAX_PLAYERS));

        // Colonna "Add to Favourite"
        favouriteButton.setCellFactory(col -> new TableCell<>() {
            private final Button favouriteBtn = new Button("Add to Favourite");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    favouriteBtn.setOnAction(event -> {
                        LobbyBean lobby = getTableView().getItems().get(getIndex());
                        controller.joinLobby(lobby, currentUser != null ? currentUser.getUsername() : "");
                    });
                    setGraphic(favouriteBtn);
                }
            }
        });

        // Colonna "Join"
        joinButtonColumn.setCellFactory(col -> new TableCell<>() {
            private final Button joinBtn = new Button("Join");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    LobbyBean lobby = getTableView().getItems().get(getIndex());
                    joinBtn.setOnAction(event -> {
                        String message = "Vuoi entrare nella lobby '" + lobby.getName() + "'?";
                        // Mostra il popup con countdown di 10 secondi
                        confirmationPopupController.show(
                                message,
                                10,
                                () -> joinLobby(lobby), // azione da eseguire se conferma
                                () -> System.out.println("Azione annullata o scaduta.") // azione da eseguire se annulla o scade
                        );
                    });
                    setGraphic(joinBtn);
                }
            }
        });

        lobbyTableView.setItems(filteredLobbies);
    }

    private void doFilter() {
        String type = comboBox1.getValue();
        String duration = comboBox2.getValue();
        String numPlayers = comboBox3.getValue();
        List<LobbyBean> result = controller.filterLobbies(type, duration, numPlayers);
        filteredLobbies.setAll(result);
    }

    private void joinLobby(LobbyBean lobby) {
        boolean joined = controller.joinLobby(lobby, currentUser != null ? currentUser.getUsername() : "");
        if (!joined) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile entrare nella lobby.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    //cambi scena
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
