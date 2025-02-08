package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.exception.PopupLoadingException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;

import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.application.Platform;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;

public class ManageLobbyListGuestBoundary extends ManageLobbyListBoundary {

    // Il pannello principale definito nel file FXML (deve essere presente in FXML con fx:id="manageLobbyListPane")
    @FXML



    protected AnchorPane characterPane;



    protected ConfirmationPopupController confirmationPopupController;

    /**
     * Metodo di inizializzazione che richiama l'inizializzazione del parent e, subito dopo,
     * mostra un pannello centrato contenente un messaggio, un contatore e un pulsante.
     * Viene avviato un timer di 5 secondi che reindirizza automaticamente al login.
     */
    @Override
    @FXML
    public void initialize() {
        super.initialize();

        // Se l'utente è guest e non ha una lista, inizializzala come vuota
        if (currentUser == null) {
            currentUser = new UserBean("guest@example.com","guest" ,RoleEnum.GUEST, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            logger.info(">>> currentUser inizializzato come Guest");
        } else if (currentUser.getRoleBehavior() == RoleEnum.GUEST && currentUser.getJoinedLobbies() == null) {
            currentUser.setJoinedLobbies(new ArrayList<>());
        }

        showRedirectConfirmation();

        // (Opzionale) Nascondi o disabilita altre componenti, se necessario:
        tableViewLobby.setVisible(false);
        newLobbyButton.setDisable(true);
        newLobbyButton.setVisible(false);
    }

    /**
     * Mostra il popup di conferma con il timer e reindirizza l'utente al login al termine del countdown.
     */
    private void showRedirectConfirmation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml"));
            Parent popupRoot = loader.load();
            confirmationPopupController = loader.getController();

            // Aggiungi il popup al layout
            Platform.runLater(() -> {
                manageLobbyListPane.getChildren().add(popupRoot);

                // Mostra il messaggio e imposta il timer
                confirmationPopupController.show("Stai per essere rediretto al login", 10, this::redirectToLogin, this::redirectToLogin);
            });
        } catch (IOException e) {
            throw new PopupLoadingException("Errore durante il caricamento del popup di conferma");
        }
    }

    /**
     * Metodo per eseguire il cambio scena verso il login.
     */
    private void redirectToLogin() {
        if (currentUser.getRoleBehavior() == RoleEnum.GUEST) {
            logger.info(">>> Utente guest, reindirizzamento al login...");

            // Usare Platform.runLater per posticipare il reindirizzamento al login
            Platform.runLater(() -> {
                try {
                    // Verifica che la scena sia associata al currentStage
                    Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
                    SceneSwitcher.changeScene(currentStage, SceneNames.LOGIN, currentUser);
                } catch (IOException e) {
                    logger.severe("Errore nel reindirizzamento al login: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Imposta il currentUser. Metodo definito dall'interfaccia UserAwareInterface.
     *
     * @param user il currentUser da iniettare
     */
    @Override
    public void setCurrentUser(UserBean user) {
        logger.info(">>> Impostazione currentUser: " + user.getRoleBehavior());
        this.currentUser = user;
    }
}
