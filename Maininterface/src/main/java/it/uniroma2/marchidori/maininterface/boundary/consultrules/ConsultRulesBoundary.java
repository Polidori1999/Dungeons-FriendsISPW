package it.uniroma2.marchidori.maininterface.boundary.consultrules;

import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.control.ConsultRulesController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    protected TableColumn<RuleBookBean, Button> buyButton;

    @FXML
    protected TableColumn<RuleBookBean, Button> consultButton;

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
        confirmationPopupController = ConfirmationPopupController.loadPopup(consultRulesPane);
        initRulesBookTable();
        rulesBookTableView.setItems(rulesBook);
    }

    /**
     * Inizializza la tabella con i RuleBook e configura le colonne.
     */
    private void initRulesBookTable() {
        rulesBook = controller.getAllRuleBooks();

        // Colonna "Nome RuleBook" (riferita alla proprietà "rulesBookName")
        rulesBookNameColumn.setCellValueFactory(new PropertyValueFactory<>("rulesBookName"));

        // Colonna "Buy" (dinamica: modifica testo e stato in base al bean)
        TableColumnUtils.setupDynamicButtonColumn(buyButton,
                bean -> bean.isObtained() ? "Obtained" : "Buy now!",
                RuleBookBean::isObtained,
                this::handleBuyConfirmation);

        // Colonna "Consult" (testo statico)
        TableColumnUtils.setupButtonColumn(consultButton, "Consult Now", this::handleConsultAction);
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

    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();

        // Esegui il cambio scena
        Stage currentStage = (Stage) consultRulesPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            // Se preferisci, potresti usare un messaggio più "dinamico", come:
            // "Error during change scene from ManageLobbyListBoundary to " + fxml
            throw new SceneChangeException("Error during change scene.", e);
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
