package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.exception.PopupLoadingException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;

/**
 * Controller per il popup di conferma con timer.
 *
 */
public class ConfirmationPopupController {

    @FXML
    private AnchorPane popupPane;


    @FXML
    private Label messageLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    private TimerController timerController;
    private Runnable confirmAction;
    private Runnable cancelAction;

    @FXML
    public void initialize() {
        // Il popup è nascosto di default
        popupPane.setVisible(false);
        // Imposta i listener dei bottoni
        yesButton.setOnAction(this::onConfirm);
        noButton.setOnAction(this::onCancel);
    }

    /**
     * Mostra il popup di conferma con il messaggio, il countdown e le azioni.
     *
     * @param message       messaggio da visualizzare (es. "Vuoi entrare nella lobby X?")
     * @param duration      durata del timer in secondi
     * @param confirmAction azione da eseguire se l'utente conferma
     * @param cancelAction  azione da eseguire se l'utente annulla o scade il timer
     */

    public void show(String message, int duration, Runnable confirmAction, Runnable cancelAction) {
        this.confirmAction = confirmAction;
        this.cancelAction = cancelAction;

        messageLabel.setText(message);
        popupPane.setVisible(true);

        // Avvia il TimerController
        timerController = new TimerController(timerLabel, duration, this::onTimeOut);
        timerController.start();
    }

    private void onConfirm(ActionEvent event) {

        stopTimer();
        if (confirmAction != null) {
            confirmAction.run(); // Esegue join
        }
        hide();
    }


    private void onCancel(ActionEvent event) {
        stopTimer();
        if (cancelAction != null) {
            cancelAction.run();
        }
        hide();
    }

    private void onTimeOut() {
        stopTimer();
        if (cancelAction != null) {
            cancelAction.run();
        }
        hide();
    }

    private void stopTimer() {
        if (timerController != null) {
            timerController.stop();
        }
    }

    public void hide() {
        Platform.runLater(() -> {
            popupPane.setVisible(false);
            popupPane.setManaged(false); // Se vuoi che scompaia anche dal layout
        });
    }


    //questa versione funziona mette il pop in alto a sx
    public static ConfirmationPopupController loadPopup(AnchorPane container) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    ConfirmationPopupController.class.getResource("/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml")
            );
            Parent popupRoot = loader.load();
            container.getChildren().add(popupRoot);

            // Assicurati di eseguire il posizionamento dopo che il container è stato layouttato
            Platform.runLater(() -> {
                double layoutX = (container.getWidth() - popupRoot.prefWidth(-1)) / 2;
                double layoutY = (container.getHeight() - popupRoot.prefHeight(-1)) / 2;
                popupRoot.setLayoutX(layoutX);
                popupRoot.setLayoutY(layoutY);
            });

            return loader.getController();
        } catch (IOException e) {
            throw new PopupLoadingException("Errore durante il caricamento del popup di conferma");
        }
    }




}
