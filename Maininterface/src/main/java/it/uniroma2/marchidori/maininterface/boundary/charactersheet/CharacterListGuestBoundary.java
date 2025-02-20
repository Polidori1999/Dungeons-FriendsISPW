package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Logger;

public class CharacterListGuestBoundary extends CharacterListPlayerBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListGuestBoundary.class.getName());

    //funzione di inizializzazione uguale alla superclasse
    @Override
    public void initialize() {
        super.initialize();
    }

    //reindirizza a login
    @Override
    protected void downloadCharacter(CharacterSheetBean bean) {
        if (confirmationPopup != null) {
            String message = "Non puoi scaricare come Guest. Verrai reindirizzato al login.";
            confirmationPopup.show(message, 10,
                    this::redirectToLogin,
                    this::onDownloadCancelled
            );
        } else {
            logger.warning("ConfirmationPopup non inizializzato in GuestBoundary.");
        }
    }

    //annullamento operazione redirect to login
    private void onDownloadCancelled() {
        logger.info("Download annullato (Guest).");
    }

    //avvio redirect to login
    private void redirectToLogin() {
        try {
            Session.getInstance().clear();
            SceneSwitcher.changeScene(
                    (Stage) characterPane.getScene().getWindow(),
                    SceneNames.LOGIN,
                    null
            );
        } catch (IOException e) {
            logger.severe("Errore nel reindirizzamento al login: " + e.getMessage());
        }
    }
}
