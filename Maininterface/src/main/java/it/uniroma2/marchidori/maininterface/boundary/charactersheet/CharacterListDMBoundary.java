package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterListController;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
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

    // Il DM NON supporta la creazione, quindi questo bottone resta nascosto
    @FXML
    protected Button newCharacterButton;

    protected UserBean currentUser;
    protected CharacterListController controller;
    protected CharacterSheetBean pendingDeleteBean;
    protected ObservableList<CharacterSheetBean> data = FXCollections.observableArrayList();
    protected ConfirmationPopupController confirmationPopupController;

    protected static final Logger logger = Logger.getLogger(CharacterListDMBoundary.class.getName());


    @FXML
    protected void initialize() {
        if (controller == null) {
            logger.warning("Controller non inizializzato in DM Boundary.");
            return;
        }
        // Carica i dati (ad es. dal controller)
        data.clear();
        data.addAll(controller.getCharacterSheets());
        confirmationPopupController = ConfirmationPopupController.loadPopup(characterPane);

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

        tableViewChar.setItems(data);
    }

    protected void handleDelete(CharacterSheetBean bean) {
        if (bean != null) {
            pendingDeleteBean = bean;
            try {
                showDeleteConfirmation();
            } catch (IOException e) {
                logger.severe("Errore durante la visualizzazione del popup di conferma: " + e.getMessage());
            }
        }
    }

    private void showDeleteConfirmation() throws IOException {
        if (confirmationPopupController != null && pendingDeleteBean != null) {
            String message = "Vuoi eliminare il personaggio '" + pendingDeleteBean.getInfoBean().getName() + "'?";
            confirmationPopupController.show(
                    message,
                    10,  // timer di scadenza popup
                    () -> {
                        try {
                            onConfirmDelete();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    this::onCancelDelete
            );
        } else {
            logger.severe("Errore: ConfirmationPopupController non inizializzato o pendingDeleteBean è null");
        }
    }

    private void onConfirmDelete() throws IOException {
        String name = pendingDeleteBean.getInfoBean().getName();
        tableViewChar.getItems().remove(pendingDeleteBean);
        controller.deleteCharacter(name);
        logger.info("Personaggio '" + name + "' cancellato (DM).");
        pendingDeleteBean = null;
    }

    private void onCancelDelete() {
        logger.info("Eliminazione annullata.");
        pendingDeleteBean = null;
    }


    /**
     * Scarica il personaggio (con progress bar).
     */
    protected void downloadCharacter(CharacterSheetBean bean) {
        if (controller != null) {
            CharacterSheetDownloadTask downloadTask = controller.getDownloadTask(bean);
            if (downloadTask != null) {
                showDownloadProgressWindow(downloadTask);
                new Thread(downloadTask).start();
            } else {
                logger.severe("Task di download non disponibile.");
            }
        } else {
            logger.severe("Controller non inizializzato.");
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

        downloadTask.setOnSucceeded(event -> {
            progressStage.close();
            logger.info("Download completato (DM).");
        });
        downloadTask.setOnFailed(event -> {
            progressStage.close();
            logger.severe("Errore durante il download: " + downloadTask.getException());
        });

        Platform.runLater(progressStage::show);
    }


    public void reloadCharacterList() {
        if (currentUser != null) {
            data.clear();
            data.addAll(controller.getCharacterSheets()); // Carica i dati aggiornati dallo UserBean
            tableViewChar.refresh();
            logger.info(">>> DEBUG: Tabella ricaricata con personaggi aggiornati.");
        }
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (CharacterListController) logicController;
    }

    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @FXML
    protected void onNavigationButtonClick(javafx.event.ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();
        Stage currentStage = (Stage) characterPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena.", e);
        }
    }

    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) characterPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);

        if(fxml.equals(SceneNames.CHARACTER_LIST)){
            reloadCharacterList();
        }
    }

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