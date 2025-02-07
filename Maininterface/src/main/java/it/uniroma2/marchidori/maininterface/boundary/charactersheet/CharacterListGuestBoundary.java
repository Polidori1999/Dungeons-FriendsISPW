package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.stage.Stage;

public class CharacterListGuestBoundary extends CharacterListPlayerBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListGuestBoundary.class.getName());

    @Override
    public void initialize() {

        super.initialize();
        confirmationPopupController = ConfirmationPopupController.loadPopup(characterPane);


        newCharacterButton.setVisible(true);
        newCharacterButton.setDisable(false);
    }

    @Override
    protected void downloadCharacter(CharacterSheetBean bean) {
        if (confirmationPopupController != null) {
            String message = "you are getting redirected to login";
            confirmationPopupController.show(message, 10,
                    this::redirectToLogin,
                    this::onDelete);
        } else {
            logger.info("Errore: ConfirmationPopupController non inizializzato o pendingDeleteBean è null");
        }
    }

    private void onDelete() {
        //empty
    }


    private void redirectToLogin() {
        try {

            SceneSwitcher.changeScene((Stage) characterPane.getScene().getWindow(), "login.fxml", currentUser);
        } catch (IOException e) {
            logger.severe("Errore nel reindirizzamento al login: " + e.getMessage());
        }
    }
}
