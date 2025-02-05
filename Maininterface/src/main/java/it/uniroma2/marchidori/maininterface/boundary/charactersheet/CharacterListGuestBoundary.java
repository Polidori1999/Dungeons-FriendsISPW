package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.factory.CharacterSheetFactory;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;
import it.uniroma2.marchidori.maininterface.utils.CustomTimer;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.factory.BoundaryFactory.createBoundary;
import static it.uniroma2.marchidori.maininterface.factory.ControllerFactory.createController;

public class CharacterListGuestBoundary extends CharacterListBoundary {


    private static final Logger logger = Logger.getLogger(CharacterListGuestBoundary.class.getName());

    // Bean selezionato per l'eliminazione
    private CharacterSheetBean pendingDeleteBean;

    // Timer per la conferma di eliminazione
    private CustomTimer confirmationTimer;

    private AnchorPane redirectPane;
    private CustomTimer timer;

    private CharacterSheetController modalController;

    @Override
    public void initialize() {
        super.initialize();

        // Crea il pannello di reindirizzamento
        redirectPane = new AnchorPane();
        redirectPane.setPrefWidth(300);
        redirectPane.setPrefHeight(150);
        // Imposta lo sfondo del pannello (modifica il colore se necessario)
        redirectPane.setStyle("-fx-background-color: #ffffff;");
        redirectPane.setVisible(false);

        // Posiziona il pannello al centro del manageLobbyListPane
        updateRedirectPanePosition();
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> updateRedirectPanePosition();
        characterPane.widthProperty().addListener(sizeListener);
        characterPane.heightProperty().addListener(sizeListener);

        // Crea la Label con il messaggio
        Label messageLabel = new Label("You are getting redirected to login");
        messageLabel.setLayoutX(60);
        messageLabel.setLayoutY(30);
        messageLabel.setStyle("-fx-text-fill: black;");
        messageLabel.setVisible(false);

        // Inizializza la Label del timer
        timerLabel.setText("5s");
        timerLabel.setLayoutX(150);
        timerLabel.setLayoutY(60);
        timerLabel.setStyle("-fx-text-fill: black;");

        // Crea il pulsante "Go to login"
        Button goToLoginButton = new Button("Go to login");
        goToLoginButton.setLayoutX(115);
        goToLoginButton.setLayoutY(100);
        goToLoginButton.setStyle("-fx-text-fill: white; -fx-background-color: #e90000;");
        goToLoginButton.setOnAction((ActionEvent event) -> redirectToLogin());
        goToLoginButton.setVisible(false);

        // Aggiunge la Label, il timer e il pulsante al pannello di reindirizzamento
        redirectPane.getChildren().addAll(messageLabel, timerLabel, goToLoginButton);

        // Aggiunge il pannello di reindirizzamento al manageLobbyListPane
        characterPane.getChildren().add(redirectPane);

        // Crea e avvia il timer con 5 secondi di countdown
        timer = new CustomTimer(5, new CustomTimer.TimerListener() {
            @Override
            public void onTick(int secondsRemaining) {
                timerLabel.setText(secondsRemaining + "s");
            }

            @Override
            public void onFinish() {
                redirectToLogin();
            }
        });




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
                        messageLabel.setVisible(true);
                        redirectPane.setVisible(true);
                        goToLoginButton.setVisible(true);
                        timerLabel.setVisible(true);
                        timer.start();
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

            CharacterSheetBoundary sheetController = createBoundary(CharacterSheetBoundary.class);
            loader.setController(sheetController);

            Parent root = loader.load();

            sheetController.setCharacterSheetBean(beanToEdit);
            sheetController.setCreationMode(false);
            modalController = createController(CharacterSheetController.class);
            sheetController.setSheetController(modalController);
            sheetController.setGuestParentBoundary(this);

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

            CharacterSheetBoundary sheetController = createBoundary(CharacterSheetBoundary.class);
            loader.setController(sheetController);

            Parent root = loader.load();
            System.out.println(">>> DEBUG: FXML caricato correttamente!");

            sheetController.setCreationMode(true);
            sheetController.setCharacterSheetBean(CharacterSheetFactory.createCharacterSheet());
            modalController = createController(CharacterSheetController.class);
            sheetController.setSheetController(modalController);
            sheetController.setGuestParentBoundary(this);

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


    private void redirectToLogin() {
        try {
            Stage currentStage = (Stage) characterPane.getScene().getWindow();
            SceneSwitcher.changeScene(currentStage, SceneNames.LOGIN, currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateRedirectPanePosition() {
        double left = (characterPane.getWidth() - redirectPane.getPrefWidth()) / 2;
        double top = (characterPane.getHeight() - redirectPane.getPrefHeight()) / 2;
        // Imposta le ancore per posizionare il pannello al centro
        AnchorPane.setLeftAnchor(redirectPane, left);
        AnchorPane.setTopAnchor(redirectPane, top);
    }

}
