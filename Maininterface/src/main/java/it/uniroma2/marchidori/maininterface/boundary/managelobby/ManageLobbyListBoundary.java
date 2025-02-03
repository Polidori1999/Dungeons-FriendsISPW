package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
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

public class ManageLobbyListBoundary implements UserAwareInterface {

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
    protected TableView<LobbyBean> tableViewLobby;

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
    protected void initialize() {
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
    }


    /**
     * Metodo che inizializza la TableView e configura le colonne.
     */

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to consult rules.", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby to home.", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to join lobby.", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to manage lobby.", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to character list.", e);
        }
    }


    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from manage lobby list to user.", e);
        }
    }
    @FXML
    void onClickNewLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.NEW_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a newLobby.fxml", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

}
