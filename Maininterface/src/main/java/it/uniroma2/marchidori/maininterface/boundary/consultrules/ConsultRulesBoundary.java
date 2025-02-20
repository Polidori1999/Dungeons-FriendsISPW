package it.uniroma2.marchidori.maininterface.boundary.consultrules;

import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ConfirmationPopup;
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
    protected ConsultRulesController controller;//reference al controller relativo
    protected ConfirmationPopup confirmationPopup;

    private static final Logger logger = Logger.getLogger(ConsultRulesBoundary.class.getName());


    //Bean selezionato in attesa di conferma dell'acquisto.

    protected RuleBookBean pendingBuyBean;

    //metodo di inizializzazione della GUI
    @FXML
    public void initialize() {
        confirmationPopup = ConfirmationPopup.loadPopup(consultRulesPane);
        initRulesBookTable();
        rulesBookTableView.setItems(rulesBook);
    }

    //riempimento tableview
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

    //metodo per consultare un libro di regole
    private void handleConsultAction(RuleBookBean book) {
        pendingBuyBean = book;
        if (book.isObtained()) {
            controller.openFileIfExists(book.getPath());
        } else if (confirmationPopup != null) {
            String message = "Vuoi comprare il libro: " + book.getRulesBookName() + "?";
            confirmationPopup.show(message, 10, this::onConfirm, this::onCancel);
        }
    }

    //confirmtaion op up per buy
    private void handleBuyConfirmation(RuleBookBean bean) {
        pendingBuyBean = bean;
        if (confirmationPopup != null && pendingBuyBean != null) {
            String message = "Vuoi comprare il libro: " + pendingBuyBean.getRulesBookName() + "?";
            confirmationPopup.show(message, 10, this::onConfirm, this::onCancel);
        } else {
            logger.log(Level.WARNING, "Errore: popup o bean non inizializzato correttamente.");
        }
    }

    //annulla operazione
    private void onCancel() {
        pendingBuyBean = null;
    }


    // Chiamato quando l'utente conferma l'acquisto dal popup.
    //delego al controller paypal
    private void onConfirm() {
        if (pendingBuyBean != null) {
            double price = 0.01;
            // Avvia la procedura di pagamento PayPal
            controller.startPayPalPayment(price);
            pendingBuyBean.setObtained(true);
            // Aggiornare la tabella
            rulesBookTableView.refresh();
            pendingBuyBean = null;
        }
    }

//funzione per la navigazione tra le scene GUI
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

    //funzioni per realizzazzione userawareinterface e controllerawareinterface
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ConsultRulesController) logicController;
    }
}
