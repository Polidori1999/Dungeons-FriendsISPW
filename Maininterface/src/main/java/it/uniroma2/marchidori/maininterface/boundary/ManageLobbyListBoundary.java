package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageLobbyListBoundary{

    @FXML
    protected AnchorPane manageLobbyListPane;

    @FXML
    private Button consultRules;

    @FXML
    private Button joinlobby;

    @FXML
    private Button manageLobby;

    @FXML
    private Button myChar;

    @FXML
    private VBox vBox;

    @FXML
    private Button goToHome;

    @FXML
    protected Button newLobbyButton;

    @FXML
    private TableColumn<LobbyBean, String> tableViewDuration;

    @FXML
    private TableColumn<LobbyBean, String> tableViewLiveOrNot;

    @FXML
    private TableView<LobbyBean> tableViewLobby;

    @FXML
    private TableColumn<LobbyBean, Button> tableViewLobbyDelete;

    @FXML
    protected TableColumn<LobbyBean, Button> tableViewLobbyEdit;

    @FXML
    private TableColumn<LobbyBean, String> tableViewLobbyName;

    @FXML
    private TableColumn<LobbyBean, String> tableViewMaxPlayers;

    @FXML
    private Button userButton;

    // Variabile per gestire l'utente corrente
    protected UserBean currentUser;

    /**
     * Invocato automaticamente da JavaFX dopo l'iniezione dei nodi @FXML.
     */
    @FXML
    protected void initialize(URL url, ResourceBundle rb) {
        configureUI();
    }

    /**
     * Configurazione di base: inizializzazione della TableView e delle colonne.
     */
    protected void configureUI() {
        initCommonTableView();
    }

    /**
     * Metodo che inizializza la TableView e configura le colonne.
     */
    private void initCommonTableView() {
        // Imposta il PropertyValueFactory per le colonne basate sulle proprietà di LobbyBean.
        tableViewLobbyName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewMaxPlayers.setCellValueFactory(new PropertyValueFactory<>("maxPlayers"));
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tableViewLiveOrNot.setCellValueFactory(new PropertyValueFactory<>("liveOrNot"));


        tableViewLobbyDelete.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        // Rimuoviamo il Bean dalla tabella
                        LobbyBean selectedLobby = getTableView().getItems().get(getIndex());
                        getTableView().getItems().remove(selectedLobby);
                    });
                }
            }
        });

        // Se necessario, imposta i dati nella TableView (ad es. da un ObservableList)
        // tableViewLobby.setItems(...);
    }

    @FXML
    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
        try {
            changeScene("consultRules.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to consult rules.", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene("home.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to home.", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene("joinLobby.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to join lobby.", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene("manageLobbyList.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to manage lobby.", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene("characterList.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to character list.", e);
        }
    }


    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene("user.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to user.", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }
}
