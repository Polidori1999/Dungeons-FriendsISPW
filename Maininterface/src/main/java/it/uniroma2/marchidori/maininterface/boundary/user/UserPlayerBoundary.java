package it.uniroma2.marchidori.maininterface.boundary.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserPlayerBoundary extends UserBoundary {

    private static final Logger LOGGER = Logger.getLogger(UserPlayerBoundary.class.getName());

    @Override
    protected void configureUI() {
        // Richiama la logica base che setta userName, roleUser, emailUser
        super.configureUI();
        // Cambia etichetta del bottone
        switchRoleButton.setText("Switch Role to DM");
    }

    @Override
    @FXML
    protected void onClickSwitchRole(ActionEvent event) throws IOException {
        // 1) Cambia il ruolo nel model
        currentUser.switchRole();   // <-- usa il field PROTECTED ereditato
        LOGGER.log(Level.INFO, () -> "Switched role to: " + currentUser.getRoleBehavior().getRoleName());

        // 2) Carica user.fxml ma assegnando come controller la classe DM
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/it/uniroma2/marchidori/maininterface/user.fxml")
        );

        // 3) Crea una nuova istanza del controller DM
        UserDMBoundary newController = new UserDMBoundary();
        // Passa l'utente
        newController.setCurrentUser(currentUser);

        // 4) Imposta manualmente il controller
        loader.setController(newController);

        // 5) Carica
        Parent root = loader.load();

        // 6) Mostra la nuova scena
        Stage stage = (Stage) userPane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
