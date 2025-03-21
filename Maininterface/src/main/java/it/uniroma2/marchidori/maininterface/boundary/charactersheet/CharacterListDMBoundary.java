package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterListController;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetDownloadController;
import it.uniroma2.marchidori.maininterface.boundary.ConfirmationPopup;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

import java.util.logging.Logger;

public class CharacterListDMBoundary implements ControllerAwareInterface, UserAwareInterface {


    //elementi fxml per la GUI
    @FXML
    protected AnchorPane characterPane;

    @FXML
    protected TableView<CharacterSheetBean> tableViewChar;

    @FXML
    protected TableColumn<CharacterSheetBean, String> tableViewCharName;
    @FXML
    protected TableColumn<CharacterSheetBean, String> tableViewCharRace;
    @FXML
    protected TableColumn<CharacterSheetBean, String> tableViewCharLevel;
    @FXML
    protected TableColumn<CharacterSheetBean, String> tableViewCharClass;

    // Colonne per Delete e Download
    @FXML
    protected TableColumn<CharacterSheetBean, Button> tableViewCharDelete;
    @FXML
    protected TableColumn<CharacterSheetBean, Button> tableViewCharDownloadButton;
    //bottone per il new character
    @FXML
    protected Button newCharacterButton;

    protected UserBean currentUser; //
    protected CharacterListController controller;
    protected CharacterSheetBean pendingDeleteBean;
    protected ObservableList<CharacterSheetBean> data = FXCollections.observableArrayList();
    protected ConfirmationPopup confirmationPopup;


    protected static final Logger logger = Logger.getLogger(CharacterListDMBoundary.class.getName());


    @FXML
    protected void initialize() {
        if (controller == null) {
            logger.warning("Controller non inizializzato in DM Boundary.");
            return;
        }

        data.clear();
        data.addAll(controller.getCharacterSheets());//riempimento lista data con le schede personaggi
        confirmationPopup = ConfirmationPopup.loadPopup(characterPane); //caricamento del popup

        // Imposta le colonne base
        tableViewCharName.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("name"));
        tableViewCharRace.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("race"));
        tableViewCharLevel.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("level"));
        tableViewCharClass.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("classe"));

        // Setup delle colonne Delete e Download
        TableColumnUtils.setupButtonColumn(tableViewCharDelete, "Delete", this::handleDelete);
        TableColumnUtils.setupButtonColumn(tableViewCharDownloadButton, "Download", this::downloadCharacter);

        // Il DM non supporta la creazione: nasconde il bottone New Character
        newCharacterButton.setVisible(false);
        newCharacterButton.setDisable(true);

        tableViewChar.setItems(data);//inserimento dei dati nella tableview
    }

    //gestione del delete richiedo conferma e poi delego al controller
    protected void handleDelete(CharacterSheetBean bean) {
        if (bean != null) {
            pendingDeleteBean = bean;
            showDeleteConfirmation();
        }
    }

    //visualizzazione e richiesta di conferma
    protected void showDeleteConfirmation() {
        if (confirmationPopup != null && pendingDeleteBean != null) {
            String message = "Vuoi eliminare il personaggio '" + pendingDeleteBean.getInfoBean().getName() + "'?";
            confirmationPopup.show(
                    message,
                    10,  // timer di scadenza popup
                    this::onConfirmDelete,
                    this::onCancelDelete
            );
        } else {
            logger.severe("Errore: ConfirmationPopup non inizializzato o pendingDeleteBean è null");
        }
    }

    //delego al controller l operazione di delete
    protected void onConfirmDelete() {
        String name = pendingDeleteBean.getInfoBean().getName();
        tableViewChar.getItems().remove(pendingDeleteBean);
        controller.deleteCharacter(name);
        pendingDeleteBean = null;
    }

    //annullo operazione
    protected void onCancelDelete() {
        pendingDeleteBean = null;
    }




    //GUI download e con istanziazzione e delegazione al controller
    protected void downloadCharacter(CharacterSheetBean bean) {
        CharacterSheetDownloadController downloadController;
        downloadController = new CharacterSheetDownloadController();
        CharacterSheetDownloadTask downloadTask = downloadController.getDownloadTask(bean);
        if (downloadTask != null) {
            showDownloadProgressWindow(downloadTask);
            new Thread(downloadTask).start();
        } else {
            logger.severe("Task di download non disponibile.");
        }
    }

    //parte grafica no logica
    protected void showDownloadProgressWindow(CharacterSheetDownloadTask downloadTask) {
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle("Download in corso...");

        Label infoLabel = new Label("Download in corso...");
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(downloadTask.progressProperty());

        VBox vbox = new VBox(10, infoLabel, progressBar);
        vbox.setStyle("-fx-padding: 10; -fx-background-color: white;");
        progressStage.setScene(new Scene(vbox, 350, 100));

        downloadTask.setOnSucceeded(event ->
            progressStage.close()
        );
        downloadTask.setOnFailed(event -> {
            progressStage.close();
            logger.severe("Errore durante il download: " + downloadTask.getException());
        });

        Platform.runLater(progressStage::show);
    }


    //refresh della lista data e riempimento della tableview
    public void reloadCharacterList() {
        if (currentUser != null) {
            data.clear();
            data.addAll(controller.getCharacterSheets()); // Carica i dati aggiornati dallo UserBean
            tableViewChar.refresh();
        }
    }

    //funzioni di realizzazione delle interfaccie useraware e controlleraware
    @Override
    public void setLogicController(Object logicController) {
        this.controller = (CharacterListController) logicController;
    }

    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }



    //funzione per la navigazione tra le boundary delega al sceneSwitcher il cambio di scena
    @FXML
    protected void onNavigationButtonClick(javafx.event.ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();
        Stage currentStage = (Stage) characterPane.getScene().getWindow();
        if(SceneNames.CHARACTER_SHEET.equals(fxml)) {
            currentUser.setSelectedLobbyName(null);
        }
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
            reloadCharacterList();
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena.", e);
        }
    }

    //change scene che invoca lo SceneSwitcher dedicato ad una sola funzione
    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) characterPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);

        if(fxml.equals(SceneNames.CHARACTER_LIST)){
            reloadCharacterList();
        }
    }


    //settings per la tableview
    private record ReadOnlyObjectWrapperFactory<S>(String propertyName)
            implements Callback<TableColumn.CellDataFeatures<S, String>, ObservableValue<String>> {

        @Override
        public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<S, String> param) {
            S rowItem = param.getValue();

            // Accertiamoci che l'oggetto nella riga sia un CharacterSheetBean
            if (rowItem instanceof CharacterSheetBean bean) {
                // In base al "propertyName", ritorniamo il valore appropriato
                return switch (propertyName) {
                    case "name" -> new ReadOnlyObjectWrapper<>(bean.getInfoBean().getName());
                    case "race" -> new ReadOnlyObjectWrapper<>(bean.getInfoBean().getRace());
                    case "level" -> new ReadOnlyObjectWrapper<>(String.valueOf(bean.getInfoBean().getLevel()));
                    case "classe" -> new ReadOnlyObjectWrapper<>(bean.getInfoBean().getClasse());
                    // se per qualche ragione arriva un propertyName sconosciuto, ritorna "???"
                    default -> new ReadOnlyObjectWrapper<>("???");
                };
            }
            return new ReadOnlyObjectWrapper<>("???");
        }
    }
}