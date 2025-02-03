package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher; // Importa SceneSwitcher
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.util.List;

public class JoinLobbyBoundary implements UserAwareInterface {

    @FXML
    private AnchorPane joinLobbyPane;

    // ComboBox per i filtri (type, durata, numPlayers)
    @FXML
    private ComboBox<String> comboBox1; // "Online"/"Presenza"
    @FXML
    private ComboBox<String> comboBox2; // "Singola"/"Campagna"
    @FXML
    private ComboBox<String> comboBox3; // "2..8"

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

    // **TableView** e colonne
    @FXML
    private TableView<LobbyBean> lobbyTableView;
    @FXML
    private TableColumn<LobbyBean, String> lobbyNameColumn;
    @FXML
    private TableColumn<LobbyBean, Void> joinButtonColumn;
    @FXML
    private TableColumn<LobbyBean, Void> favouriteButton;

    private UserBean currentUser;

    // Controller di logica per la gestione dei filtri e del join
    private JoinLobbyController joinLobbyController;

    // Lista osservabile contenente le lobby filtrate
    private ObservableList<LobbyBean> filteredLobbies;

    @FXML
    public void initialize() {
        // 1) Inizializziamo il JoinLobbyController
        joinLobbyController = new JoinLobbyController();

        // 2) Popoliamo i ComboBox
        comboBox1.setItems(FXCollections.observableArrayList("Online", "Presenza"));
        comboBox2.setItems(FXCollections.observableArrayList("Singola", "Campagna"));
        comboBox3.setItems(FXCollections.observableArrayList("2", "3", "4", "5", "6", "7", "8"));

        // 3) Listener per filtrare appena cambia un ComboBox
        comboBox1.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        comboBox2.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        comboBox3.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());

        // 4) Carichiamo le lobby iniziali (tutte)
        List<LobbyBean> initial = joinLobbyController.filterLobbies(null, null, null);
        filteredLobbies = FXCollections.observableArrayList(initial);

        // 5) Inizializziamo la TableView
        initTableView();
    }

    /**
     * Inizializza la TableView, le sue colonne e collega la ObservableList.
     */
    private void initTableView() {
        // Colonna "Lobby Name"
        // Usa "name" perché in LobbyBean esiste getName()
        lobbyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Colonna con il pulsante "favourite"
        favouriteButton.setCellFactory(col -> new TableCell<>() {

            private Button favouriteBtn;

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                initFavouriteButtonIfNeeded();
                setGraphic(favouriteBtn);
            }

            private void initFavouriteButtonIfNeeded() {
                if (favouriteBtn == null) {
                    favouriteBtn = new Button("Add to Favourite");
                    favouriteBtn.setOnAction(event -> handleFavouriteAction());
                }
            }

            private void handleFavouriteAction() {
                // Recupera la riga su cui è stato cliccato
                LobbyBean lobby = getTableView().getItems().get(getIndex());
                // Logica per "aggiungere ai preferiti" / "joinare" la lobby
                joinLobbyController.joinLobby(lobby, "");
            }
        });



        joinButtonColumn.setCellFactory(col -> new TableCell<>() {
            private Button joinBtn;  // pulsante non inizializzato subito

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Istanzia il pulsante solo la prima volta
                    if (joinBtn == null) {
                        joinBtn = new Button("Join");
                        joinBtn.setOnAction(event -> {
                            // Recupera la riga su cui è stato cliccato
                            LobbyBean lobby = getTableView().getItems().get(getIndex());
                            // Logica per "joinare" la lobby
                            joinLobbyController.joinLobby(lobby, "");
                        });
                    }
                    setGraphic(joinBtn);
                }
            }
        });


        // 6) Colleghiamo l'ObservableList alla TableView
        lobbyTableView.setItems(filteredLobbies);
    }

    /**
     * Legge i valori dei ComboBox e invoca il JoinLobbyController per filtrare.
     */
    private void doFilter() {
        // Prendiamo i filtri dai ComboBox
        String type = comboBox1.getValue();      // "Online"/"Presenza"
        String duration = comboBox2.getValue();  // "Singola"/"Campagna"
        String numPlayers = comboBox3.getValue(); // "2..8"

        // Recuperiamo la lista filtrata
        List<LobbyBean> result = joinLobbyController.filterLobbies(type, duration, numPlayers);
        // Aggiorniamo la collezione osservabile, che aggiornerà la TableView
        filteredLobbies.setAll(result);
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
    private void changeScene(String fxml) throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) joinLobbyPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
