package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.factory.CharacterSheetFactory;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;
import it.uniroma2.marchidori.maininterface.utils.CustomTimer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class CharacterListPlayerBoundary extends CharacterListBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListPlayerBoundary.class.getName());


    // Bean selezionato per l'eliminazione
    private CharacterSheetBean pendingDeleteBean;

    // Timer per la conferma di eliminazione
    private CustomTimer confirmationTimer;

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
        data.addAll(controller.getAllCharacters());
        tableViewChar.refresh();

        System.out.println(">>> DEBUG: Numero di personaggi nella tabella: " + data.size());

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
                        openEditCharacterModal(selectedChar);
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
                        // Salva il bean selezionato per cancellazione
                        pendingDeleteBean = getTableView().getItems().get(getIndex());
                        // Visualizza il confirmation panel e avvia il timer
                        showConfirmationPanel();
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
                        // Avvia il download simulato
                        downloadCharacter(selectedChar);
                    });
                }
            }
        });
    }

    // ===========================================================
    //                  HANDLER PER DELETE
    // ===========================================================
    /**
     * Mostra il confirmation panel e avvia un timer di 10 secondi.
     */
    private void showConfirmationPanel() {
        confirmationPane.setVisible(true);
        confirmationLabel.setVisible(true);
        yesButton.setVisible(true);
        yesButton.setDisable(false);
        noButton.setVisible(true);
        noButton.setDisable(false);
        timerLabel.setVisible(true);

        timerLabel.setText("10s");
        timerLabel.setTextFill(Paint.valueOf("#ffffff"));

        // Avvio un timer di 10 secondi con CustomTimer
        if (confirmationTimer != null) {
            confirmationTimer.stop();
        }
        confirmationTimer = new CustomTimer(10, new CustomTimer.TimerListener() {
            @Override
            public void onTick(int secondsRemaining) {
                timerLabel.setText(secondsRemaining + "s");
            }
            @Override
            public void onFinish() {
                onClickNo(new ActionEvent());
            }
        });
        confirmationTimer.start();
    }

    @FXML
    void onClickYes(ActionEvent event) {
        if (confirmationTimer != null) {
            confirmationTimer.stop();
        }
        if (pendingDeleteBean != null) {
            String characterName = pendingDeleteBean.getInfoBean().getName();
            tableViewChar.getItems().remove(pendingDeleteBean);
            controller.deleteCharacter(characterName);
            pendingDeleteBean = null;
        }
        hideConfirmationPanel();
    }

    @FXML
    void onClickNo(ActionEvent event) {
        if (confirmationTimer != null) {
            confirmationTimer.stop();
        }
        pendingDeleteBean = null;
        hideConfirmationPanel();
    }

    /**
     * Nasconde il confirmation panel.
     */
    private void hideConfirmationPanel() {
        confirmationPane.setVisible(false);
        confirmationLabel.setVisible(false);
        yesButton.setVisible(false);
        noButton.setVisible(false);
        timerLabel.setVisible(false);
    }

    // ===========================================================
    //                 HANDLER PER EDIT
    // ===========================================================
    private void openEditCharacterModal(CharacterSheetBean beanToEdit) {
        try {
            System.out.println(">>> DEBUG: Avvio modifica personaggio: "
                    + beanToEdit.getInfoBean().getName());

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));

            CharacterSheetBoundary sheetController = new CharacterSheetBoundary();
            loader.setController(sheetController);

            Parent root = loader.load();

            sheetController.setCharacterSheetBean(beanToEdit);
            sheetController.setCreationMode(false);
            sheetController.setController(controller);
            sheetController.setParentBoundary(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Modifica Personaggio");
            modalStage.initOwner(characterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.setScene(new Scene(root));

            System.out.println(">>> DEBUG: Finestra di modifica aperta!");
            modalStage.showAndWait();

            // Aggiorna la tabella
            tableViewChar.refresh();

        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica personaggio).", e);
        }
    }

    // ===========================================================
    //               HANDLER PER NUOVO PERSONAGGIO
    // ===========================================================
    @FXML
    void onClickNewCharacter(ActionEvent event) {
        openCreateCharacterModal();
    }

    private void openCreateCharacterModal() {
        try {
            System.out.println(">>> DEBUG: Avvio caricamento finestra modale per nuovo personaggio...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));

            CharacterSheetBoundary sheetController = new CharacterSheetBoundary();
            loader.setController(sheetController);

            Parent root = loader.load();
            System.out.println(">>> DEBUG: FXML caricato correttamente!");

            sheetController.setCreationMode(true);
            sheetController.setCharacterSheetBean(CharacterSheetFactory.createCharacterSheet());
            sheetController.setController(controller);
            sheetController.setParentBoundary(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Crea Nuovo Personaggio");
            modalStage.initOwner(characterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.setScene(new Scene(root));

            System.out.println(">>> DEBUG: Mostro la finestra modale...");
            modalStage.showAndWait();

            System.out.println(">>> DEBUG: Finestra modale chiusa, aggiorno la tabella...");
            tableViewChar.refresh();

        } catch (IOException e) {
            System.err.println(">>> ERRORE: IOException durante il caricamento di characterSheet.fxml!");
            e.printStackTrace();
            throw new SceneChangeException("Errore nel cambio scena (nuovo personaggio).", e);
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
    private void downloadCharacter(CharacterSheetBean bean) {
        // Scegli la posizione e il nome del file
        // (modifica se necessario o usa un file chooser)
        String fileName = "character_" + bean.getInfoBean().getName() + ".txt";
        String destinationPath = "C:/Users/edoar/IdeaProjects/Dungeons-FriendsISPW/Maininterface/src/main/java/download/" + fileName; // Esempio di percorso fisso

        // Crea la finestra con la ProgressBar
        ProgressBar progressBar = new ProgressBar(0);
        Label infoLabel = new Label("Downloading " + bean.getInfoBean().getName() + "...");
        infoLabel.setStyle("-fx-text-fill: black;");

        VBox vbox = new VBox(10, infoLabel, progressBar);
        vbox.setStyle("-fx-background-color: #ffffff; -fx-padding: 10;");

        Stage downloadStage = new Stage();
        downloadStage.setTitle("Download in progress...");
        downloadStage.initModality(Modality.APPLICATION_MODAL);
        downloadStage.initOwner(characterPane.getScene().getWindow());
        downloadStage.setScene(new Scene(vbox, 300, 120));

        // Crea il task di download
        CharacterSheetDownloadTask downloadTask = new CharacterSheetDownloadTask(bean, destinationPath);

        // Collega la ProgressBar al progresso del task
        progressBar.progressProperty().bind(downloadTask.progressProperty());

        // Quando il download termina con successo, chiudi la finestra
        downloadTask.setOnSucceeded(evt -> {
            downloadStage.close();
            System.out.println("Download completato. File salvato in: " + destinationPath);
        });

        // Se fallisce, chiudi la finestra e stampa l'errore
        downloadTask.setOnFailed(evt -> {
            downloadStage.close();
            System.err.println("Errore durante il download: " + downloadTask.getException());
        });

        // Mostra la finestra e avvia il task in un nuovo thread
        downloadStage.show();
        new Thread(downloadTask).start();
    }
}
