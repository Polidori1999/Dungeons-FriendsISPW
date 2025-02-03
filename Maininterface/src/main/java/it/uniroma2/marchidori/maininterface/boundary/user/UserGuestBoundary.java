package it.uniroma2.marchidori.maininterface.boundary.user;

import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class UserGuestBoundary extends UserBoundary{
    @Override
    protected void configureUI() {
        // Richiama la logica base che setta userName, roleUser, emailUser
        super.configureUI();
        roleUser.setText(currentUser.getRoleBehavior().getRoleName());
        // Cambia etichetta del bottone
        switchRoleButton.setText("Login");
        System.out.println("eccopmi qua sono u guest");
    }

    @FXML
    protected void onClickSwitchRole(ActionEvent event) throws IOException {
        changeScene(SceneNames.LOGIN);
    }

}
