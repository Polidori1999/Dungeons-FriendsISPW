package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class JoinLobbyBoundary implements Initializable {

    @FXML
    private AnchorPane joinLobbyPane;

    // ComboBox per i filtri (type, durata, numPlayers)
    @FXML
    private ComboBox<String> ComboBox1; // "Online"/"Presenza"
    @FXML
    private ComboBox<String> ComboBox2; // "Singola"/"Campagna"
    @FXML
    private ComboBox<String> ComboBox3; // "2..8"

    @FXML
    private Button Consult_rules;
    @FXML
    private Button Joinlobby;
    @FXML
    private Button ManageLobby;
    @FXML
    private Button Mychar;
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

    // Controller di logica per la gestione dei filtri e del join
    private JoinLobbyController joinLobbyController;

    // Lista osservabile contenente le lobby filtrate
    private ObservableList<LobbyBean> filteredLobbies;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // 1) Inizializziamo il JoinLobbyController
        joinLobbyController = new JoinLobbyController();

        // 2) Popoliamo i ComboBox
        ComboBox1.setItems(FXCollections.observableArrayList("Online", "Presenza"));
        ComboBox2.setItems(FXCollections.observableArrayList("Singola", "Campagna"));
        ComboBox3.setItems(FXCollections.observableArrayList("2", "3", "4", "5", "6", "7", "8"));

        // 3) Listener per filtrare appena cambia un ComboBox
        ComboBox1.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        ComboBox2.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        ComboBox3.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());

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
            private final Button favouriteBtn = new Button("Add to Favourite");

            {
                // Azione per il pulsante "Join"
                favouriteBtn.setOnAction((ActionEvent event) -> {
                    // Recupera la riga su cui è stato cliccato
                    LobbyBean lobby = getTableView().getItems().get(getIndex());
                    // Chiama la logica per "joinare" la lobby
                    joinLobbyController.joinLobby(lobby,"");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(favouriteBtn);
                }
            }
        });
        joinButtonColumn.setCellFactory(col -> new TableCell<>() {
            private final Button joinBtn = new Button("Join");

            {
                // Azione per il pulsante "Join"
                joinBtn.setOnAction((ActionEvent event) -> {
                    // Recupera la riga su cui è stato cliccato
                    LobbyBean lobby = getTableView().getItems().get(getIndex());
                    // Chiama la logica per "joinare" la lobby
                    joinLobbyController.joinLobby(lobby,"");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
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
        String type = ComboBox1.getValue();      // "Online"/"Presenza"
        String duration = ComboBox2.getValue();  // "Singola"/"Campagna"
        String numPlayers = ComboBox3.getValue(); // "2..8"

        // Recuperiamo la lista filtrata
        List<LobbyBean> result = joinLobbyController.filterLobbies(type, duration, numPlayers);
        // Aggiorniamo la collezione osservabile, che aggiornerà la TableView
        filteredLobbies.setAll(result);
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) throws IOException {
        try {
            changeScene("consultRules.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene("home.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene("joinLobby.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene("manageLobby.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene("user.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene("characterList.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        // Carica il file FXML della seconda scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/" + fxml));
        Parent root = loader.load();

        // Ottieni lo stage attuale
        Stage stage = (Stage) joinLobbyPane.getScene().getWindow(); // Alternativa: (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
