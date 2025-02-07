package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class CharacterListGuestBoundary extends CharacterListPlayerBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListGuestBoundary.class.getName());

    @Override
    public void initialize() {

        super.initialize();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml"));
            Parent popupRoot = loader.load();
            confirmationPopupController = loader.getController();
            characterPane.getChildren().add(popupRoot);
        } catch (IOException e) {
            logger.severe("Errore nel caricamento del popup di conferma: " + e.getMessage());
        }

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
