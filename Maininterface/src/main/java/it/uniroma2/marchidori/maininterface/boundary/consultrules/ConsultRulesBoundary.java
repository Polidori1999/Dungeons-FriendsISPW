package it.uniroma2.marchidori.maininterface.boundary.consultrules;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.control.ConsultRulesController;
import it.uniroma2.marchidori.maininterface.control.PayPalPaymentController;
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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final Jout jout = new Jout(this.getClass().getSimpleName());


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

    private void initRulesBookTable() {
        rulesBook = controller.getAllRuleBooks();
        rulesBookNameColumn.setCellValueFactory(new PropertyValueFactory<>("rulesBookName"));

        // Colonna "Buy" (dinamica)
        TableColumnUtils.setupDynamicButtonColumn(buyButton,
                bean -> bean.isObtained() ? "Obtained" : "Buy now!",
                RuleBookBean::isObtained,
                this::handleBuyConfirmation);

        // Colonna "Consult" (sempre "Consult Now")
        TableColumnUtils.setupButtonColumn(consultButton, "Consult Now", this::handleConsultAction);
    }

    private void handleConsultAction(RuleBookBean book) {
        pendingBuyBean = book;
        if (book.isObtained()) {
            controller.openFileIfExists(book.getPath());
        } else if (confirmationPopupController != null) {
            String message = "Vuoi comprare il libro: " + book.getRulesBookName() + "?";
            confirmationPopupController.show(message, 10, this::onConfirm, this::onCancel);
        }
    }

    private void handleBuyConfirmation(RuleBookBean bean) {
        pendingBuyBean = bean;
        if (confirmationPopupController != null && pendingBuyBean != null) {
            String message = "Vuoi comprare il libro: " + pendingBuyBean.getRulesBookName() + "?";
            confirmationPopupController.show(message, 10, this::onConfirm, this::onCancel);
        } else {
            logger.log(Level.WARNING, "Errore: popup o bean non inizializzato correttamente.");
        }
    }

    private void onCancel() {
        pendingBuyBean = null;
    }

    /**
     * Chiamato quando l'utente conferma l'acquisto dal popup.
     * Qui puoi inserire la logica PayPal (creazione ordine, apertura link).
     */
    private void onConfirm() {
        if (pendingBuyBean != null) {
            // ESEMPIO: Prezzo fisso (oppure puoi derivarlo dal bean)
            double price = 0.01;
            // Avvia la procedura di pagamento PayPal
            startPayPalPayment(price);

            // Se vuoi segnare subito come "obtained":
            pendingBuyBean.setObtained(true);
            // Oppure, se preferisci attendere la conferma dal server PayPal,
            // potresti farlo DOPO la cattura.
            // Per semplicità, lo facciamo subito:

            // Aggiornare la tabella
            rulesBookTableView.refresh();
            pendingBuyBean = null;
        }
    }

    /**
     * Esempio di metodo che richiama PayPalPaymentController per creare un ordine
     * e aprire il link di checkout nel browser di sistema.
     */
    private void startPayPalPayment(double amount) {
        try {
            PayPalPaymentController payCtrl = new PayPalPaymentController();

            // 1) Ottieni access token
            String accessToken = payCtrl.getAccessToken();
            jout.print("AccessToken = " + accessToken);

            // 2) Crea ordine (EUR e importo a piacere)
            String createOrderResponse = payCtrl.createOrder(accessToken, "EUR", String.valueOf(amount));
            jout.print("createOrderResponse = " + createOrderResponse);

            // 3) Estrai link "approve"
            String approveLink = payCtrl.extractApproveLink(createOrderResponse);
            if (approveLink == null) {
                logger.log(Level.SEVERE, "Impossibile trovare il link di approvazione nel JSON di risposta PayPal");
                return;
            }

            // 4) Apri il browser con il link PayPal
            // In un'app desktop JavaFX, puoi fare:
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new java.net.URI(approveLink));
            } else {
                logger.warning("Apertura browser non supportata su questo sistema!");
            }

            // A questo punto l'utente vede la pagina PayPal, effettua il login e completa il pagamento.
            // Se desideri catturare in un secondo momento, dovrai salvare l'orderID e
            // chiamare "capture" su /v2/checkout/orders/{orderID}/capture.

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il pagamento PayPal: ", e);

            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();

        Stage currentStage = (Stage) consultRulesPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene.", e);
        }
    }

    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) consultRulesPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ConsultRulesController) logicController;
    }
}
