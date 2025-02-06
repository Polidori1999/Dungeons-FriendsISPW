package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.event.EventHandler;
import java.io.IOException;
import java.util.logging.Logger;


public class CharacterListPlayerBoundary extends CharacterListBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListPlayerBoundary.class.getName());

    // Bean selezionato per l'eliminazione
    private CharacterSheetBean pendingDeleteBean;
    private CharacterSheetController modalController;

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

        System.out.println(">>> DEBUG: Numero di personaggi nella tabella: " + data.size());

        // Carica il popup di conferma dal file FXML e aggiungilo al contenitore principale
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml"));
            Parent popupRoot = loader.load();
            // Si assume che "characterPane" (definito in CharacterListBoundary) sia il contenitore principale
            characterPane.getChildren().add(popupRoot);
            confirmationPopupController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ---------------------------------------------------
        // Colonna EDIT (bottone "Edit")
        // ---------------------------------------------------
        tableViewCharButton.setCellValueFactory(cellData -> {
            Button editBtn = new Button("Edit");
            return new ReadOnlyObjectWrapper<>(editBtn);
        });
        tableViewCharButton.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        CharacterSheetBean selectedChar = getTableView().getItems().get(getIndex());
                        System.out.println(">>> DEBUG: Bottone Edit premuto per personaggio: "
                                + selectedChar.getInfoBean().getName());
                        editChar(selectedChar);
                    });
                }
            }
        });

        // ---------------------------------------------------
        // Colonna DELETE (bottone "Delete")
        // ---------------------------------------------------
        tableViewCharDelete.setCellValueFactory(cellData -> {
            Button deleteBtn = new Button("Delete");
            return new ReadOnlyObjectWrapper<>(deleteBtn);
        });
        tableViewCharDelete.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        // Salva il bean selezionato per la cancellazione
                        pendingDeleteBean = getTableView().getItems().get(getIndex());
                        // Mostra il popup di conferma con timer
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        showDeleteConfirmation();
                    });
                }
            }
        });

        // ---------------------------------------------------
        // Colonna DOWNLOAD (bottone "Download")
        // ---------------------------------------------------
        tableViewCharDownloadButton.setCellValueFactory(cellData -> {
            Button downloadBtn = new Button("Download");
            return new ReadOnlyObjectWrapper<>(downloadBtn);
        });
        tableViewCharDownloadButton.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        // Ottieni il CharacterSheetBean dalla riga
                        CharacterSheetBean selectedChar = getTableView().getItems().get(getIndex());
                        downloadCharacter(selectedChar);
                    });
                }
            }
        });
    }

    /**
     * Mostra il popup di conferma per l'eliminazione del personaggio.
     */
    private void showDeleteConfirmation() {
        if (confirmationPopupController != null && pendingDeleteBean != null) {
            String message = "Vuoi eliminare il personaggio '" + pendingDeleteBean.getInfoBean().getName() + "'?";
            confirmationPopupController.show(message, 10,
                    () -> onConfirmDelete(),
                    () -> onCancelDelete());
        } else {
            System.err.println("Errore: ConfirmationPopupController non inizializzato o pendingDeleteBean è null");
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
            System.out.println(">>> Aggiungendo il nuovo personaggio alla tabella...");
            data.add(newBean);
            tableViewChar.refresh();
        } else {
            System.err.println(">>> ERRORE: newBean è NULL in addNewCharacterBean()!");
        }
    }

    public void addCharacterToTable(CharacterSheetBean character) {
        data.add(character);
        tableViewChar.refresh();
        System.out.println(">>> DEBUG: Personaggio aggiunto alla tabella: "
                + character.getInfoBean().getName());
    }

    public void updateExistingCharacterInTable(CharacterSheetBean updatedCharacter) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getInfoBean().getName().equals(updatedCharacter.getInfoBean().getName())) {
                data.set(i, updatedCharacter);
                break;
            }
        }
        tableViewChar.refresh();
        System.out.println(">>> DEBUG: Personaggio aggiornato nella tabella: "
                + updatedCharacter.getInfoBean().getName());
    }

    @Override
    public void refreshTable() {
        tableViewChar.refresh();
    }

    @Override
    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
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
                System.err.println("Errore: Task di download non disponibile.");
            }
        } else {
            System.err.println("Errore: Controller non inizializzato.");
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
        downloadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent event) {
                progressStage.close();
                System.out.println(">>> Download completato.");
            }
        });

        downloadTask.setOnFailed(event -> {
            progressStage.close();
            System.err.println("Errore durante il download: " + downloadTask.getException());
        });

        // Mostra la finestra
        Platform.runLater(progressStage::show);
    }
}
