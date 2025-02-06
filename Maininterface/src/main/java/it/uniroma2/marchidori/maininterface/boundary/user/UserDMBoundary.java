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


public class UserDMBoundary extends UserBoundary {

    private static final Logger LOGGER = Logger.getLogger(UserDMBoundary.class.getName());

    @Override
    protected void configureUI() {
        super.configureUI();
        // Cambia etichetta del bottone
        switchRoleButton.setText("Switch Role to Player");
    }

    @Override
    @FXML
    protected void onClickSwitchRole(ActionEvent event) throws IOException {
        // 1) Cambia ruolo nel model
        controller.switchRole(currentUser);
        controller.switchRole(currentEntity);
        LOGGER.log(Level.INFO, () -> "Switched role to: " + currentUser.getRoleBehavior().getRoleName());

        // 2) Carica user.fxml ma con controller Player
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/it/uniroma2/marchidori/maininterface/user.fxml")
        );

        UserPlayerBoundary newController = new UserPlayerBoundary();
        newController.setCurrentUser(currentUser);

        loader.setController(newController);

        Parent root = loader.load();

        Stage stage = (Stage) userPane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
