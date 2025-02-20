package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.control.TimerController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


// Controller grafico per il popup di conferma con timer.

public class ConfirmationPopup {

    private static final Logger logger = Logger.getLogger(ConfirmationPopup.class.getName());

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

    private TimerController timerController;//reference al controller del timer
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


    //Mostra il popup di conferma con il messaggio, il countdown e le azioni.

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
            confirmAction.run();
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


    //Carica e posiziona il popup all'interno di un container
    public static ConfirmationPopup loadPopup(AnchorPane container) {
        ConfirmationPopup popup = null;
        try {
            FXMLLoader loader = new FXMLLoader(
                    ConfirmationPopup.class.getResource("/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml")
            );
            Parent popupRoot = loader.load();
            container.getChildren().add(popupRoot);
            popup = loader.getController();

            Platform.runLater(() -> {
                double layoutX = (container.getWidth() - popupRoot.prefWidth(-1)) / 2;
                double layoutY = (container.getHeight() - popupRoot.prefHeight(-1)) / 2;
                popupRoot.setLayoutX(layoutX);
                popupRoot.setLayoutY(layoutY);
            });
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return popup;
    }
}