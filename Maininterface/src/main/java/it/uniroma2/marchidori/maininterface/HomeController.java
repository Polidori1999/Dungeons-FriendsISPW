package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML
    private AnchorPane HomePane;

    // ComboBox per filtri: type, durata, numPlayers
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

    // ListView che mostrerà i LobbyBean
    @FXML
    private ListView<LobbyBean> lobbyListView;

    // Controller BCE che gestisce la logica di filtri/join
    private JoinLobbyController joinLobbyController;

    // Collezione di LobbyBean filtrati
    private ObservableList<LobbyBean> filteredLobbies;

    @FXML
    public void initialize() {
        // 1) Creiamo il JoinLobbyController PRIMA di usarlo!
        joinLobbyController = new JoinLobbyController();

        // 2) Popoliamo i ComboBox
        ComboBox1.setItems(FXCollections.observableArrayList("Online", "Presenza"));
        ComboBox2.setItems(FXCollections.observableArrayList("Singola", "Campagna"));
        ComboBox3.setItems(FXCollections.observableArrayList("2", "3", "4", "5", "6", "7", "8"));

        // 3) Listener per filtrare appena cambia un ComboBox
        ComboBox1.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        ComboBox2.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        ComboBox3.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());

        // 4) Primo caricamento (filtro vuoto => tutte le lobby)
        List<LobbyBean> initial = joinLobbyController.filterLobbies(null, null, null);
        filteredLobbies = FXCollections.observableArrayList(initial);
        lobbyListView.setItems(filteredLobbies);
    }

    /**
     * Legge i valori dei ComboBox e invoca il JoinLobbyController per filtrare.
     */
    private void doFilter() {
        String type = ComboBox1.getValue();
        String duration = ComboBox2.getValue();
        String numPlayers = ComboBox3.getValue();

        List<LobbyBean> result = joinLobbyController.filterLobbies(type, duration, numPlayers);
        filteredLobbies.setAll(result);
    }

    // -------------------------------
    //       Metodi di navigazione
    // -------------------------------

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        goToLogin();
    }

    private void goToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) HomePane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) throws IOException {
        goToMyChar();
    }

    private void goToMyChar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterList.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) HomePane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    // (Altri metodi per "Joinlobby", "ManageLobby", ecc., se li implementerai)
}
