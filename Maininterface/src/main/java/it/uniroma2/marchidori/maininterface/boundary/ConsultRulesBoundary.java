package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.ObservableList;
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

public class ConsultRulesBoundary implements UserAwareInterface {

    private UserBean currentUser;

    @FXML
    private AnchorPane consultRulesPane;

    @FXML
    private TableColumn<RuleBookBean, Void> buyButton;

    @FXML
    private TableColumn<RuleBookBean, Void> consultButton;

    @FXML
    private Button consultRules;

    @FXML
    private Button home;

    @FXML
    private Button joinLobby;

    @FXML
    private Button manageLobby;

    @FXML
    private Button myChar;

    @FXML
    private TableColumn<RuleBookBean, String> ownedColumn;

    @FXML
    private TableColumn<RuleBookBean, String> rulesBookNameColumn;

    @FXML
    private TableView<RuleBookBean> rulesBookTableView;

    @FXML
    private Button userButton;

    @FXML
    private VBox vBox;

    private ObservableList<RuleBookBean> rulesBook;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        initTableView();
    }

    /**
     * Inizializza la TableView, le sue colonne e collega la ObservableList.
     */
    private void initTableView() {
        // Colonna "Lobby Name"
        // Usa "name" perché in LobbyBean esiste getName()
        rulesBookNameColumn.setCellValueFactory(new PropertyValueFactory<>("Rule's book name"));

        // Colonna con il pulsante "buy"
        buyButton.setCellFactory(col -> new TableCell<>() {

            private Button buyBtn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                initFavouriteButtonIfNeeded();
                setGraphic(buyBtn);
            }

            private void initFavouriteButtonIfNeeded() {
                if (buyBtn == null) {
                    buyBtn = new Button("buy now!");
                    buyBtn.setOnAction(event -> handleBuyAction());
                }
            }

            private void handleBuyAction() {
                // Recupera la riga su cui è stato cliccato
                //RuleBookBean book = getTableView().getItems().get(getIndex());
            }
        });



        consultButton.setCellFactory(col -> new TableCell<>() {
            private Button consultBtn;  // pulsante non inizializzato subito

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Istanzia il pulsante solo la prima volta
                    if (consultBtn == null) {
                        consultBtn = new Button("Consult Now");
                        consultBtn.setOnAction(event -> {
                            // Recupera la riga su cui è stato cliccato
                            //RuleBookBean book = getTableView().getItems().get(getIndex());
                            // Logica per consultare

                        });
                    }
                    setGraphic(consultBtn);
                }
            }
        });


        // 6) Colleghiamo l'ObservableList alla TableView
        rulesBookTableView.setItems(rulesBook);
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
        Stage currentStage = (Stage) consultRulesPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}