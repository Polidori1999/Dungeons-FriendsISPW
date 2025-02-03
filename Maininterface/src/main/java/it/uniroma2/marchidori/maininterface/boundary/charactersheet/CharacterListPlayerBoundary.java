package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.logging.Logger;

public class CharacterListPlayerBoundary extends CharacterListBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListPlayerBoundary.class.getName());


    // Variabili per il timer
    private int seconds = 10;
    private Timeline timeline;

    // Variabile per salvare il bean in attesa di eliminazione
    private CharacterSheetBean pendingDeleteBean;

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
        data.addAll(controller.getAllCharacters()); // Ritorna i Bean convertiti da entityToBean
        tableViewChar.refresh();

        System.out.println(">>> DEBUG: Numero di personaggi nella tabella: " + data.size());

        // Colonna EDIT
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
                        System.out.println(">>> DEBUG: Bottone Edit premuto per personaggio: " + selectedChar.getInfoBean().getName());
                        openEditCharacterModal(selectedChar);
                    });
                }
            }
        });

        // Colonna DELETE: Mostra il confirmation panel con timer
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

    }

    /**
     * Mostra il confirmation panel, il messaggio e il timer, quindi avvia il countdown.
     */
    private void showConfirmationPanel() {
        confirmationPane.setVisible(true);
        confirmationLabel.setVisible(true);
        yesButton.setVisible(true);
        yesButton.setDisable(false);
        noButton.setVisible(true);
        noButton.setDisable(false);
        timerLabel.setVisible(true);

        // Imposta il messaggio (se necessario) e il timer
        timerLabel.setText(seconds + "s");
        startTimer();
    }

    /**
     * Avvia un timer di 10 secondi che aggiorna la label. Se il tempo scade, annulla l'operazione.
     */
    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds--;
            timerLabel.setText(seconds + "s");

            if (seconds <= 0) {
                timeline.stop();
                // Se il timer scade, annulla l'operazione
                onClickNo(new ActionEvent());
            }
        }));
        timeline.setCycleCount(10);
        timeline.play();
    }

    /**
     * Metodo invocato quando l'utente clicca il bottone Yes.
     * Procede con la cancellazione del bean.
     */
    @FXML
    void onClickYes(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }
        if (pendingDeleteBean != null) {
            // Rimuove il bean dalla TableView
            tableViewChar.getItems().remove(pendingDeleteBean);
            // Se necessario, invoca anche il metodo sul controller per la cancellazione definitiva:
            // controller.deleteCharacter(pendingDeleteBean);
            pendingDeleteBean = null;
        }
        hideConfirmationPanel();
    }

    /**
     * Metodo invocato quando l'utente clicca No o quando il timer scade.
     * Annulla l'operazione di cancellazione.
     */
    @FXML
    void onClickNo(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }
        pendingDeleteBean = null;
        hideConfirmationPanel();
    }

    /**
     * Nasconde il confirmation panel e ripristina lo stato degli elementi.
     */
    private void hideConfirmationPanel() {
        confirmationPane.setVisible(false);
        confirmationLabel.setVisible(false);
        yesButton.setVisible(false);
        noButton.setVisible(false);
        timerLabel.setVisible(false);
    }

    private void openEditCharacterModal(CharacterSheetBean beanToEdit) {
        try {
            System.out.println(">>> DEBUG: Avvio modifica personaggio: "
                    + beanToEdit.getInfoBean().getName());

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));

            // Crea manualmente il Controller e assegna PRIMA di caricare
            CharacterSheetBoundary sheetController = new CharacterSheetBoundary();
            loader.setController(sheetController);

            Parent root = loader.load();

            // PASSA I DATI AL CONTROLLER
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

            // Aggiorna la tabella dopo la modifica
            tableViewChar.refresh();

        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica personaggio).", e);
        }
    }

    public void addNewCharacterBean(CharacterSheetBean newBean) {
        if (newBean != null) {
            System.out.println(">>> Aggiungendo il nuovo personaggio alla tabella...");
            data.add(newBean);
            tableViewChar.refresh();
        } else {
            System.err.println(">>> ERRORE: newBean è NULL in addNewCharacterBean()!");
        }
    }

    @FXML
    void onClickNewCharacter(ActionEvent event) {
        openCreateCharacterModal();
    }

    private void openCreateCharacterModal() {
        try {
            System.out.println(">>> DEBUG: Avvio caricamento finestra modale per nuovo personaggio...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));

            // Imposta il controller prima di caricare
            CharacterSheetBoundary sheetController = new CharacterSheetBoundary();
            loader.setController(sheetController);

            Parent root = loader.load();
            System.out.println(">>> DEBUG: FXML caricato correttamente!");

            // Imposta i dati nel controller
            sheetController.setCreationMode(true);
            sheetController.setCharacterSheetBean(new CharacterSheetBean(
                    new CharacterInfoBean("", "", 0, "", 1),
                    new CharacterStatsBean(10, 10, 10, 10, 10, 10)
            ));
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

    // Aggiunge un nuovo personaggio alla tabella
    public void addCharacterToTable(CharacterSheetBean character) {
        data.add(character);
        tableViewChar.refresh();
        System.out.println(">>> DEBUG: Personaggio aggiunto alla tabella: "
                + character.getInfoBean().getName());
    }

    // Aggiorna un personaggio esistente nella tabella
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

    // Ricarica completamente la tabella
    @Override
    public void refreshTable() {
        tableViewChar.refresh();
    }

    @Override
    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }
}