package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterListPlayerBoundary extends CharacterListBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListPlayerBoundary.class.getName());

    // Bean selezionato per l'eliminazione
    protected CharacterSheetBean pendingDeleteBean;
    protected CharacterSheetController modalController;

    // Controller per il popup di conferma con timer
    protected ConfirmationPopupController confirmationPopupController;

    @Override
    public void initialize() {
        super.initialize();

        newCharacterButton.setVisible(true);
        newCharacterButton.setDisable(false);

        if (controller == null) {
            logger.severe("Errore: controller non inizializzato!");
            return;
        }
        data.clear();
        data.addAll(currentUser.getCharacterSheets());
        tableViewChar.refresh();
        logger.log(Level.INFO, ">>> DEBUG: Numero di personaggi nella tabella: {0}", data.size());
        // Carica il popup di conferma dal file FXML e aggiungilo al contenitore principale
        confirmationPopupController = ConfirmationPopupController.loadPopup(characterPane);
        // Configura la colonna "Edit"
        TableColumnUtils.setupButtonColumn(tableViewCharButton, "Edit", this::editChar);

        // Configura la colonna "Delete"
        TableColumnUtils.setupButtonColumn(tableViewCharDelete, "Delete", characterSheet -> {
            pendingDeleteBean = characterSheet;
            showDeleteConfirmation();
        });

        // ---------------------------------------------------
        // Colonna DOWNLOAD (bottone "Download")
        // Utilizza il metodo della classe utility per impostare il pulsante statico "Download"
        // ed eseguire l'azione download sul CharacterSheetBean della riga.
        // ---------------------------------------------------
        TableColumnUtils.setupButtonColumn(tableViewCharDownloadButton, "Download", this::downloadCharacter);
    }

    /**
     * Mostra il popup di conferma per l'eliminazione del personaggio.
     */
    private void showDeleteConfirmation() {
        if (confirmationPopupController != null && pendingDeleteBean != null) {
            String message = "Vuoi eliminare il personaggio '" + pendingDeleteBean.getInfoBean().getName() + "'?";
            confirmationPopupController.show(message, 10,
                    this::onConfirmDelete,
                    this::onCancelDelete);
        } else {
            logger.severe("Errore: ConfirmationPopupController non inizializzato o pendingDeleteBean è null");
        }
    }

    private void onConfirmDelete() {
        if (pendingDeleteBean != null) {
            String characterName = pendingDeleteBean.getInfoBean().getName();
            tableViewChar.getItems().remove(pendingDeleteBean);
            controller.deleteCharacter(characterName);

            pendingDeleteBean = null;
        }
    }

    private void onCancelDelete() {
        pendingDeleteBean = null;
    }

    private void editChar(CharacterSheetBean beanToEdit) {
        // Imposta in userBean il nome della lobby da editare
        currentUser.setSelectedLobbyName(beanToEdit.getInfoBean().getName());
        // Passo alla scena "manageLobby.fxml"
        try {
            SceneSwitcher.changeScene(
                    (Stage) characterPane.getScene().getWindow(),
                    SceneNames.CHARACTER_SHEET,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica Lobby).", e);
        }
    }

    // ===========================================================
    //               HANDLER PER NUOVO PERSONAGGIO
    // ===========================================================
    @FXML
    void onClickNewCharacter(ActionEvent event) {
        // Invece di aprire un modal, usiamo la scena "manageLobby.fxml"
        // e settiamo selectedLobbyName = null => creazione
        currentUser.setSelectedLobbyName(null);
        try {
            SceneSwitcher.changeScene(
                    (Stage) characterPane.getScene().getWindow(),
                    SceneNames.CHARACTER_SHEET,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (nuova lobby).", e);
        }
    }

    // ===========================================================
    //               METODI PER AGGIORNARE LA TABELLA
    // ===========================================================
    public void addNewCharacterBean(CharacterSheetBean newBean) {
        if (newBean != null) {
            logger.info(">>> Aggiungendo il nuovo personaggio alla tabella...");
            data.add(newBean);
            tableViewChar.refresh();
        } else {
            logger.severe(">>> ERRORE: newBean è NULL in addNewCharacterBean()!");
        }
    }

    public void addCharacterToTable(CharacterSheetBean character) {
        data.add(character);
        tableViewChar.refresh();
        logger.info(">>> DEBUG: Personaggio aggiunto alla tabella: " + character.getInfoBean().getName());
    }

    public void updateExistingCharacterInTable(CharacterSheetBean updatedCharacter) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getInfoBean().getName().equals(updatedCharacter.getInfoBean().getName())) {
                data.set(i, updatedCharacter);

                break;
            }
        }
        tableViewChar.refresh();
        logger.info(">>> DEBUG: Personaggio aggiornato nella tabella: " + updatedCharacter.getInfoBean().getName());
    }

    @Override
    public void refreshTable() {
        tableViewChar.refresh();
    }

    // ===========================================================
    //              LOGICA DI DOWNLOAD DEL PERSONAGGIO
    // ===========================================================
    /**
     * Avvia il download simulato del CharacterSheetBean, creando un file di testo.
     * Mostra una finestra con una ProgressBar che aggiorna l'avanzamento del download.
     */
    protected void downloadCharacter(CharacterSheetBean bean) {
        if (controller != null) {
            CharacterSheetDownloadTask downloadTask = controller.getDownloadTask(bean);
            if (downloadTask != null) {
                showDownloadProgressWindow(downloadTask);
                new Thread(downloadTask).start();
            } else {
                logger.severe("Errore: Task di download non disponibile.");
            }
        } else {
            logger.severe("Errore: Controller non inizializzato.");
        }
    }

    private void showDownloadProgressWindow(CharacterSheetDownloadTask downloadTask) {
        // Crea una nuova finestra di avanzamento
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle("Download in corso...");

        // Creazione UI
        Label infoLabel = new Label("Scaricamento in corso...");
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(downloadTask.progressProperty());

        VBox vbox = new VBox(10, infoLabel, progressBar);
        vbox.setStyle("-fx-background-color: #ffffff; -fx-padding: 10;");
        progressStage.setScene(new Scene(vbox, 350, 100));

        // Chiudi la finestra automaticamente al completamento del download
        downloadTask.setOnSucceeded(event -> {
            progressStage.close();
            logger.info(">>> Download completato.");
        });

        downloadTask.setOnFailed(event -> {
            progressStage.close();
            logger.severe("Errore durante il download: " + downloadTask.getException());
        });

        // Mostra la finestra
        Platform.runLater(progressStage::show);
    }
}
