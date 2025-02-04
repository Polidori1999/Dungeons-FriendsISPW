package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.factory.ControllerFactory;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher; // Importa SceneSwitcher
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.CustomTimer;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;

public class JoinLobbyBoundary implements UserAwareInterface {

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

    // **TableView** e colonne
    @FXML
    protected TableView<LobbyBean> lobbyTableView;
    @FXML
    protected TableColumn<LobbyBean, String> lobbyNameColumn;
    @FXML
    protected TableColumn<LobbyBean, String> playersColumn;

    @FXML
    protected TableColumn<LobbyBean, Void> joinButtonColumn;
    @FXML
    protected TableColumn<LobbyBean, Void> favouriteButton; // pulsante non inizializzato subito


    private UserBean currentUser;

    // Controller di logica per la gestione dei filtri e del join
    private JoinLobbyController joinLobbyController;

    // Lista osservabile contenente le lobby filtrate
    private ObservableList<LobbyBean> filteredLobbies;

    public JoinLobbyBoundary() {
        this(ControllerFactory.createJoinLobbyController());
    }

    // Costruttore esplicito se vogliamo passare un controller specifico (utile per test)
    public JoinLobbyBoundary(JoinLobbyController joinLobbyController) {
        this.joinLobbyController = joinLobbyController;
    }

    @FXML
    public void initialize() {
        this.joinLobbyController = ControllerFactory.createJoinLobbyController();
        // 1) Popoliamo i ComboBox
        comboBox1.setItems(FXCollections.observableArrayList("Online", "Presenza"));
        comboBox2.setItems(FXCollections.observableArrayList("Singola", "Campagna"));
        comboBox3.setItems(FXCollections.observableArrayList("2", "3", "4", "5", "6", "7", "8"));

        // 2) Listener per filtrare appena cambia un ComboBox
        comboBox1.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        comboBox2.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());
        comboBox3.valueProperty().addListener((obs, oldVal, newVal) -> doFilter());

        // 3) Carichiamo le lobby iniziali (tutte)
        List<LobbyBean> initial = joinLobbyController.filterLobbies(null, null, null);
        filteredLobbies = FXCollections.observableArrayList(initial);

        // 4) Inizializziamo la TableView
        initTableView();
    }

    /**
     * Inizializza la TableView, le sue colonne e collega la ObservableList.
     */
    private void initTableView() {
        // Colonna "Lobby Name" – usa "name" perché in LobbyBean esiste getName()
        lobbyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //colonna "players" mostra currentPlayers/8
        playersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumberOfPlayers()+"/8"));

        // Colonna con il pulsante "Add to Favourite"
        favouriteButton.setCellFactory(col -> new TableCell<>() {
            private Button favouriteBtn;

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }
                if (favouriteBtn == null) {
                    favouriteBtn = new Button("Add to Favourite");
                    favouriteBtn.setOnAction(event -> handleFavouriteAction());
                }
                setGraphic(favouriteBtn);
            }

            private void handleFavouriteAction() {
                // Recupera la riga su cui è stato cliccato
                LobbyBean lobby = getTableView().getItems().get(getIndex());
                // Logica per "aggiungere ai preferiti" / "joinare" la lobby
                joinLobbyController.joinLobby(lobby, "");
            }
        });

        // Colonna con il pulsante "Join"
        joinButtonColumn.setCellFactory(col -> new TableCell<>() {
            private Button joinBtn;

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    if (joinBtn == null) {
                        joinBtn = new Button("Join");
                        joinBtn.setOnAction(event -> {
                            // Recupera la riga su cui è stato cliccato
                            LobbyBean lobby = getTableView().getItems().get(getIndex());
                            // Mostra il popup di conferma con timer
                            showJoinPrompt(lobby);
                        });
                    }
                    setGraphic(joinBtn);
                }
            }
        });

        // Colleghiamo l'ObservableList alla TableView
        lobbyTableView.setItems(filteredLobbies);
    }

    /**
     * Legge i valori dei ComboBox e invoca il JoinLobbyController per filtrare.
     */
    private void doFilter() {
        String type = comboBox1.getValue();      // "Online"/"Presenza"
        String duration = comboBox2.getValue();    // "Singola"/"Campagna"
        String numPlayers = comboBox3.getValue();  // "2..8"

        List<LobbyBean> result = joinLobbyController.filterLobbies(type, duration, numPlayers);
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
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) joinLobbyPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    /**
     * Mostra un pannello di conferma personalizzato con timer (10s) per confermare il join alla lobby.
     */
    private void showJoinPrompt(LobbyBean lobby) {
        // Nascondiamo la TableView mentre il popup è attivo
        lobbyTableView.setVisible(false);

        // Crea il pannello di conferma
        AnchorPane confirmationPane = new AnchorPane();
        confirmationPane.setPrefWidth(350);
        confirmationPane.setPrefHeight(160);
        confirmationPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width: 1px;");

        // Posiziona il pannello al centro del joinLobbyPane
        updateConfirmationPanePosition(confirmationPane);
        // Aggiorna la posizione se la finestra cambia dimensione
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> updateConfirmationPanePosition(confirmationPane);
        joinLobbyPane.widthProperty().addListener(sizeListener);
        joinLobbyPane.heightProperty().addListener(sizeListener);

        // Messaggio di conferma
        Label messageLabel = new Label("Vuoi entrare nella lobby: " + lobby.getName() + "?");
        messageLabel.setLayoutX(80);
        messageLabel.setLayoutY(30);
        messageLabel.setStyle("-fx-text-fill: black;");

        // Label per il timer
        Label timerLabel = new Label("10s");
        timerLabel.setLayoutX(160);
        timerLabel.setLayoutY(60);
        timerLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");

        // Pulsante "Join"
        Button joinButton = new Button("Join");
        joinButton.setLayoutX(80);
        joinButton.setLayoutY(100);
        joinButton.setStyle("-fx-text-fill: white; -fx-background-color: #008000;");
        // Quando si preme "Join", proviamo a effettuare il join e chiudiamo il popup
        joinButton.setOnAction(event -> {
            customTimerStopAndJoin(lobby, timerLabel);  // metodo helper che ferma il timer e tenta il join
            closeConfirmationPane(confirmationPane);
        });

        // Pulsante "Cancel"
        Button cancelButton = new Button("Cancel");
        cancelButton.setLayoutX(180);
        cancelButton.setLayoutY(100);
        cancelButton.setStyle("-fx-text-fill: white; -fx-background-color: #e90000;");
        cancelButton.setOnAction(event -> {
            // Ferma il timer e chiudi il popup senza effettuare join
            customTimerStop(timerLabel);
            closeConfirmationPane(confirmationPane);
        });

        confirmationPane.getChildren().addAll(messageLabel, timerLabel, joinButton, cancelButton);
        joinLobbyPane.getChildren().add(confirmationPane);

        // Avvia il timer con countdown di 10 secondi
        CustomTimer timer = new CustomTimer(10, new CustomTimer.TimerListener() {
            @Override
            public void onTick(int secondsRemaining) {
                timerLabel.setText(secondsRemaining + "s");
            }

            @Override
            public void onFinish() {
                closeConfirmationPane(confirmationPane);
            }
        });
        timer.start();
    }

    /**
     * Posiziona il pannello di conferma al centro del joinLobbyPane.
     */
    private void updateConfirmationPanePosition(AnchorPane confirmationPane) {
        double left = (joinLobbyPane.getWidth() - confirmationPane.getPrefWidth()) / 2;
        double top = (joinLobbyPane.getHeight() - confirmationPane.getPrefHeight()) / 2;
        AnchorPane.setLeftAnchor(confirmationPane, left);
        AnchorPane.setTopAnchor(confirmationPane, top);
    }

    /**
     * Chiude il pannello di conferma e ripristina la visibilità della TableView.
     */
    private void closeConfirmationPane(AnchorPane confirmationPane) {
        joinLobbyPane.getChildren().remove(confirmationPane);
        lobbyTableView.setVisible(true);
    }

    /**
     * Metodo helper per fermare il timer e tentare il join alla lobby.
     * Se il join fallisce, mostra un alert di errore.
     */
    private void customTimerStopAndJoin(LobbyBean lobby, Label timerLabel) {
        // Poiché il timer è locale nel metodo showJoinPrompt, qui si può gestire la logica di join
        boolean joined = joinLobbyController.joinLobby(lobby, (currentUser != null) ? currentUser.getUsername() : "");
        System.out.println("Tentativo di join per lobby: " + lobby.getName() + " da parte di " + ((currentUser != null) ? currentUser.getUsername() : "unknown"));

        if (!joined) {
            showErrorAlert("Impossibile entrare nella lobby (piena o inesistente).");
        }
    }

    /**
     * Metodo helper per fermare il timer.
     * (Nel nostro caso, non abbiamo un riferimento globale al timer,
     *  quindi questo metodo è solo indicativo se volessi gestirlo in altro modo.)
     */
    private void customTimerStop(Label timerLabel) {
        // Se hai un riferimento al timer, lo fermi qui.
        // In questo esempio il timer viene stoppato direttamente nei gestori dei pulsanti.
    }

    /**
     * Mostra un alert di errore utilizzando la classe Alert (personalizzata) esistente.
     */
    private void showErrorAlert(String msg) {
        // Utilizza la classe Alert del package it.uniroma2.marchidori.maininterface.utils
        // per mostrare un messaggio di errore.
        it.uniroma2.marchidori.maininterface.utils.Alert.showError("Error", msg);
    }
}
