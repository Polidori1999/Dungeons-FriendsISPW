package it.uniroma2.marchidori.maininterface.boundary.consultrules;

import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.control.ConsultRulesController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Boundary per la consultazione dei manuali (RuleBook).
 */
public class ConsultRulesBoundary implements UserAwareInterface, ControllerAwareInterface {

    private UserBean currentUser;

    @FXML
    private AnchorPane consultRulesPane;

    @FXML
    protected TableColumn<RuleBookBean, Void> buyButton;

    @FXML
    protected TableColumn<RuleBookBean, Void> consultButton;

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
    protected TableColumn<RuleBookBean, String> ownedColumn;

    @FXML
    protected TableColumn<RuleBookBean, String> rulesBookNameColumn;

    @FXML
    protected TableView<RuleBookBean> rulesBookTableView;

    @FXML
    private Button userButton;

    @FXML
    private VBox vBox;

    protected ObservableList<RuleBookBean> rulesBook;
    protected ConsultRulesController controller;
    protected ConfirmationPopupController confirmationPopupController;

    private static final Logger logger = Logger.getLogger(ConsultRulesBoundary.class.getName());

    /**
     * Bean selezionato in attesa di conferma dell'acquisto.
     */
    protected RuleBookBean pendingBuyBean;

    @FXML
    public void initialize() {
        loadConfirmationPopup();
        initRulesBookTable();
        rulesBookTableView.setItems(rulesBook);
    }

    /**
     * Carica il popup di conferma e inizializza il relativo controller.
     */
    private void loadConfirmationPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml"));
            Parent popupRoot = loader.load();
            consultRulesPane.getChildren().add(popupRoot);
            confirmationPopupController = loader.getController();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante il caricamento del popup di conferma: {0}", e.getMessage());
        }
    }

    /**
     * Inizializza la tabella con i RuleBook e configura le colonne.
     */
    private void initRulesBookTable() {
        rulesBook = controller.getAllRuleBooks();

        // Colonna "Nome RuleBook" (riferita alla proprietà "rulesBookName")
        rulesBookNameColumn.setCellValueFactory(new PropertyValueFactory<>("rulesBookName"));

        // Colonna "Buy"
        buyButton.setCellFactory(col -> createBuyButtonCell());

        // Colonna "Consult"
        consultButton.setCellFactory(col -> createConsultButtonCell());
    }

    /**
     * Crea la cella con il pulsante "Buy".
     */
    private TableCell<RuleBookBean, Void> createBuyButtonCell() {
        return new TableCell<RuleBookBean, Void>() {

            private final Button buyBtn = new Button("Buy now!");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                // Recupera il bean relativo alla riga corrente
                RuleBookBean currentBean = getTableView().getItems().get(getIndex());
                if (currentBean.isObtained()) {
                    buyBtn.setText("Obtained");
                    buyBtn.setDisable(true);
                } else {
                    buyBtn.setText("Buy now!");
                    buyBtn.setDisable(false);
                    buyBtn.setOnAction(e -> handleBuyConfirmation(currentBean));
                }
                setGraphic(buyBtn);
            }
        };
    }

    /**
     * Crea la cella con il pulsante "Consult".
     */
    private TableCell<RuleBookBean, Void> createConsultButtonCell() {
        return new TableCell<RuleBookBean, Void>() {

            private final Button consultBtn = new Button("Consult Now");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    consultBtn.setOnAction(e -> handleConsultAction(getTableView().getItems().get(getIndex())));
                    setGraphic(consultBtn);
                }
            }
        };
    }

    /**
     * Logica per consultare un RuleBook:
     * se il libro è già ottenuto, apri il PDF; altrimenti chiedi conferma di acquisto.
     */
    private void handleConsultAction(RuleBookBean book) {
        pendingBuyBean = book;

        if (book.isObtained()) {
            openFileIfExists(book.getPath());
        } else if (confirmationPopupController != null) {
            String message = "Vuoi comprare il libro: " + book.getRulesBookName() + "?";
            confirmationPopupController.show(message, 10, this::onConfirm, this::onCancel);
        }
    }

    /**
     * Logica per gestire la richiesta di acquisto di un RuleBook.
     */
    private void handleBuyConfirmation(RuleBookBean bean) {
        pendingBuyBean = bean;
        if (confirmationPopupController != null && pendingBuyBean != null) {
            String message = "Vuoi comprare il libro: " + pendingBuyBean.getRulesBookName() + "?";
            confirmationPopupController.show(message, 10, this::onConfirm, this::onCancel);
        } else {
            logger.log(Level.WARNING, "Errore: popup o bean non inizializzato correttamente.");
        }
    }

    /**
     * Prova ad aprire il file associato al percorso indicato, se esiste.
     */
    private void openFileIfExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            logger.log(Level.WARNING, "Errore: Il percorso del file è vuoto o nullo.");
            return;
        }

        String normalizedPath = filePath.replace("/", File.separator).replace("\\", File.separator);
        File file = new File(normalizedPath);

        if (!file.exists()) {
            logger.log(Level.WARNING, "Errore: Il file non esiste! Path: {0}", normalizedPath);
            return;
        }

        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
                logger.log(Level.INFO, "File aperto con successo: {0}", normalizedPath);
            } else {
                logger.log(Level.INFO, "Il sistema non supporta l'apertura dei file.");
            }
        } catch (IOException e) {
            // Se l'errore è critico, possiamo loggare con SEVERE.
            logger.log(Level.SEVERE, String.format("Errore durante l'apertura del file: %s", normalizedPath), e);
        }
    }

    /**
     * Chiamato quando si annulla l'operazione di acquisto.
     */
    private void onCancel() {
        pendingBuyBean = null;
    }

    /**
     * Chiamato quando si conferma l'acquisto.
     * Aggiorna il bean selezionato (impostando obtained=true).
     */
    private void onConfirm() {
        if (pendingBuyBean != null) {
            pendingBuyBean.setObtained(true);
            pendingBuyBean = null;
        }
        rulesBookTableView.refresh();
    }

    /* ==============================================================
       Metodi di navigazione tra le scene
       ============================================================== */

    @FXML
    void onClickGoToConsultRules(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene to consult rules.", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene to home.", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene to join lobby.", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene to manage lobby list.", e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene to user.", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene to character list.", e);
        }
    }

    /**
     * Cambio di scena generico.
     *
     * @param fxml Nome del file FXML da caricare.
     */
    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) consultRulesPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

    /* ==============================================================
       Metodi dell'interfaccia
       ============================================================== */

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ConsultRulesController) logicController;
    }
}
