package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Logger;

public class CharacterListGuestBoundary extends CharacterListPlayerBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListGuestBoundary.class.getName());

    @Override
    public void initialize() {
        super.initialize();
        // Carica il popup di conferma per il download
        confirmationPopupController = ConfirmationPopupController.loadPopup(characterPane);
    }

    /**
     * Esegue l’override del download: invece di scaricare, reindirizza al login.
     */
    @Override
    protected void downloadCharacter(CharacterSheetBean bean) {
        if (confirmationPopupController != null) {
            String message = "Non puoi scaricare come Guest. Verrai reindirizzato al login.";
            confirmationPopupController.show(message, 10,
                    this::redirectToLogin,
                    this::onDownloadCancelled
            );
        } else {
            logger.warning("ConfirmationPopupController non inizializzato in GuestBoundary.");
        }
    }

    private void onDownloadCancelled() {
        logger.info("Download annullato (Guest).");
    }

    private void redirectToLogin() {
        try {
            SceneSwitcher.changeScene(
                    (Stage) characterPane.getScene().getWindow(),
                    "login.fxml",
                    currentUser
            );
        } catch (IOException e) {
            logger.severe("Errore nel reindirizzamento al login: " + e.getMessage());
        }
    }
}
