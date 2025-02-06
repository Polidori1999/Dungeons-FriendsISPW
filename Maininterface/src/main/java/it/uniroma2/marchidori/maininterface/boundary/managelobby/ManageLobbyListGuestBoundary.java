package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.CustomTimer;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;

public class ManageLobbyListGuestBoundary extends ManageLobbyListBoundary {

    // Il pannello principale definito nel file FXML (deve essere presente in FXML con fx:id="manageLobbyListPane")
    @FXML
    private AnchorPane manageLobbyListPane;

    private AnchorPane redirectPane;

    protected UserBean currentUser;

    // Label per visualizzare il countdown
    private Label timerLabel = new Label();

    // Timer custom per il countdown (5 secondi)
    private CustomTimer timer;

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

        // (Opzionale) Nascondi o disabilita altre componenti, se necessario:
        tableViewLobby.setVisible(false);
        newLobbyButton.setDisable(true);
        newLobbyButton.setVisible(false);

        // Crea il pannello di reindirizzamento
        redirectPane = new AnchorPane();
        redirectPane.setPrefWidth(300);
        redirectPane.setPrefHeight(150);
        // Imposta lo sfondo del pannello (modifica il colore se necessario)
        redirectPane.setStyle("-fx-background-color: #ffffff;");

        // Posiziona il pannello al centro del manageLobbyListPane
        updateRedirectPanePosition();
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> updateRedirectPanePosition();
        manageLobbyListPane.widthProperty().addListener(sizeListener);
        manageLobbyListPane.heightProperty().addListener(sizeListener);

        // Crea la Label con il messaggio
        Label messageLabel = new Label("You are getting redirected to login");
        messageLabel.setLayoutX(60);
        messageLabel.setLayoutY(30);
        messageLabel.setStyle("-fx-text-fill: black;");

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

        // Aggiunge la Label, il timer e il pulsante al pannello di reindirizzamento
        redirectPane.getChildren().addAll(messageLabel, timerLabel, goToLoginButton);

        // Aggiunge il pannello di reindirizzamento al manageLobbyListPane
        manageLobbyListPane.getChildren().add(redirectPane);

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
        timer.start();
    }

    /**
     * Aggiorna la posizione del redirectPane al centro del manageLobbyListPane.
     */
    private void updateRedirectPanePosition() {
        double left = (manageLobbyListPane.getWidth() - redirectPane.getPrefWidth()) / 2;
        double top = (manageLobbyListPane.getHeight() - redirectPane.getPrefHeight()) / 2;
        // Imposta le ancore per posizionare il pannello al centro
        AnchorPane.setLeftAnchor(redirectPane, left);
        AnchorPane.setTopAnchor(redirectPane, top);
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
