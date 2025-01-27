package it.uniroma2.marchidori.maininterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private ComboBox<String> ComboBox1; // Live/On-line
    @FXML
    private ComboBox<String> ComboBox2; // Duration
    @FXML
    private ComboBox<String> ComboBox3; // Num Players

    @FXML
    private Button Consult_rules;

    @FXML
    private AnchorPane HomePane;

    @FXML
    private Button Joinlobby;

    @FXML
    private Button ManageLobby;

    @FXML
    private Button Mychar;

    @FXML
    private ListView<Lobby> lobbyListView; // Modifica ListView per usare oggetti Lobby

    @FXML
    private Button userButton;

    // Lista simulata di lobby
    private ObservableList<Lobby> allLobbies = FXCollections.observableArrayList(
            new Lobby("Lobby1", "Online", "Singola", 4),
            new Lobby("Lobby2", "Presenza", "Campagna", 6),
            new Lobby("Lobby3", "Online", "Singola", 3),
            new Lobby("Lobby4", "Presenza", "Campagna", 8)
    );

    private ObservableList<Lobby> filteredLobbies = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Aggiungi opzioni alle ComboBox
        ComboBox1.setItems(FXCollections.observableArrayList("Online", "Presenza"));
        ComboBox2.setItems(FXCollections.observableArrayList("Singola", "Campagna"));
        ComboBox3.setItems(FXCollections.observableArrayList("2", "3", "4", "5", "6", "7", "8"));

        // Aggiungi listener per il filtro
        ComboBox1.valueProperty().addListener((obs, oldVal, newVal) -> filterLobbies());
        ComboBox2.valueProperty().addListener((obs, oldVal, newVal) -> filterLobbies());
        ComboBox3.valueProperty().addListener((obs, oldVal, newVal) -> filterLobbies());

        // Inizializza la lista filtrata con tutte le lobby
        filteredLobbies.setAll(allLobbies);
        updateLobbyView();

        // Imposta la cell factory per la ListView
        lobbyListView.setCellFactory(lv -> new ListCell<Lobby>() {
            @Override
            protected void updateItem(Lobby item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getType() + " - " + item.getDuration() + " - " + item.getNumPlayers() + " players");
                }
            }
        });
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        goToLogin();
    }

    @FXML
    private void goToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) HomePane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    private void filterLobbies() {
        // Ottieni i valori selezionati
        String type = ComboBox1.getValue();
        String duration = ComboBox2.getValue();
        String numPlayersStr = ComboBox3.getValue();

        // Filtra le lobby
        filteredLobbies.setAll(allLobbies.filtered(lobby -> {
            boolean matchesType = (type == null || lobby.getType().equals(type));
            boolean matchesDuration = (duration == null || lobby.getDuration().equals(duration));
            boolean matchesPlayers = (numPlayersStr == null || lobby.getNumPlayers() == Integer.parseInt(numPlayersStr));
            return matchesType && matchesDuration && matchesPlayers;
        }));

        // Aggiorna la vista
        updateLobbyView();
    }

    private void updateLobbyView() {
        // Aggiorna la ListView con le lobby filtrate
        lobbyListView.setItems(filteredLobbies);
    }

    // Classe per rappresentare una lobby
    public static class Lobby {
        private String name;
        private String type; // Online/Presenza
        private String duration; // Singola/Campagna
        private int numPlayers;

        public Lobby(String name, String type, String duration, int numPlayers) {
            this.name = name;
            this.type = type;
            this.duration = duration;
            this.numPlayers = numPlayers;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getDuration() {
            return duration;
        }

        public int getNumPlayers() {
            return numPlayers;
        }

        @Override
        public String toString() { //solo per visualizzare
            return name + " - " + type + " - " + duration + " - " + numPlayers + " players";
        }
    }
}
