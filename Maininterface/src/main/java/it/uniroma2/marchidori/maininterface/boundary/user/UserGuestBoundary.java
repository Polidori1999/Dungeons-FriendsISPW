package it.uniroma2.marchidori.maininterface.boundary.user;

import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UserGuestBoundary extends UserBoundary{
    @Override
    protected void initialize() {
        // Richiama la logica base che setta userName, roleUser, emailUser
        super.initialize();
        roleUser.setText(currentUser.getRoleBehavior().getRoleName());
        // Cambia etichetta del bottone
        switchRoleButton.setText("Login");

        logOutButton.setDisable(true);
        logOutButton.setVisible(false);
    }

    @FXML
    @Override
    protected void onClickSwitchRole(ActionEvent event) {
        try {
            changeScene(SceneNames.LOGIN);
        }catch(Exception e){
            throw new SceneChangeException(e.getMessage());
        }
    }

}
