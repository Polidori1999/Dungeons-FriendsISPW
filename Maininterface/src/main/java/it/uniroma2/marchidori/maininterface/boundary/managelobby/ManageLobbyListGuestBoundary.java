package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ManageLobbyListGuestBoundary extends ManageLobbyListBoundary {

    // Il pannello principale definito nel file FXML (deve essere presente in FXML con fx:id="manageLobbyListPane")
    @FXML
    private AnchorPane manageLobbyListPane;

    private AnchorPane redirectPane;

    protected UserBean currentUser;

    private int seconds = 5;
    private Timeline timeline;
    private Label timerLabel = new Label();

    /**
     * Metodo di inizializzazione che richiama l'inizializzazione del parent e, subito dopo,
     * mostra un pannello centrato contenente un messaggio, un contatore e un pulsante.
     * Viene avviato un timer di 5 secondi che reindirizza automaticamente al login.
     */
    @Override
    @FXML
    public void initialize() {
        // Richiama l'inizializzazione del parent
        super.initialize();

        // Crea il pannello di reindirizzamento
        redirectPane = new AnchorPane();
        redirectPane.setPrefWidth(300);
        redirectPane.setPrefHeight(150);
        // Imposta lo sfondo del pannello al colore #292932
        redirectPane.setStyle("-fx-background-color: #292932;");

        // Posizionamento: usa AnchorPane.setLeftAnchor e setTopAnchor,
        // e aggiungi listener per aggiornare la posizione se il contenitore cambia dimensione.
        updateRedirectPanePosition();
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> updateRedirectPanePosition();
        manageLobbyListPane.widthProperty().addListener(sizeListener);
        manageLobbyListPane.heightProperty().addListener(sizeListener);

        // Crea la Label con il messaggio
        Label messageLabel = new Label("You are getting redirected to login");
        messageLabel.setLayoutX(20);
        messageLabel.setLayoutY(30);
        messageLabel.setStyle("-fx-text-fill: white;");

        // Inizializza la Label del timer
        timerLabel.setText(seconds + "s");
        timerLabel.setLayoutX(20);
        timerLabel.setLayoutY(60);
        timerLabel.setStyle("-fx-text-fill: white;");

        // Crea il pulsante "Go to login"
        Button goToLoginButton = new Button("Go to login");
        goToLoginButton.setLayoutX(20);
        goToLoginButton.setLayoutY(100);
        goToLoginButton.setStyle("-fx-text-fill: white; -fx-background-color: #e90000;");
        goToLoginButton.setOnAction((ActionEvent event) -> redirectToLogin());

        // Aggiunge la Label, il timer e il pulsante al pannello di reindirizzamento
        redirectPane.getChildren().addAll(messageLabel, timerLabel, goToLoginButton);

        // Aggiunge il pannello di reindirizzamento al manageLobbyListPane
        manageLobbyListPane.getChildren().add(redirectPane);

        // Avvia il timer
        startTimer();
    }

    /**
     * Aggiorna la posizione del redirectPane al centro del manageLobbyListPane.
     */
    private void updateRedirectPanePosition() {
        double left = (manageLobbyListPane.getWidth() - redirectPane.getPrefWidth()) / 2;
        double top = (manageLobbyListPane.getHeight() - redirectPane.getPrefHeight()) / 2;
        AnchorPane.setLeftAnchor(redirectPane, left);
        AnchorPane.setTopAnchor(redirectPane, top);
    }

    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds--;
            // Aggiorna la label sul thread JavaFX
            Platform.runLater(() -> timerLabel.setText(seconds + "s"));
            System.out.println("Timer: " + seconds);
            if (seconds <= 0) {
                timeline.stop();
                redirectToLogin();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Metodo per eseguire il cambio scena verso il login.
     */
    private void redirectToLogin() {
        try {
            Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
            SceneSwitcher.changeScene(currentStage, SceneNames.LOGIN, currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imposta il currentUser. Metodo definito dall'interfaccia UserAwareInterface.
     *
     * @param user il currentUser da iniettare
     */
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
