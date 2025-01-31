package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;


public class ManageLobbyListPlayerBoundary extends ManageLobbyListBoundary{

    private UserBean currentUser;

    @Override
    protected void configureUI() {
        // Chiama la configurazione di base definita nella superclasse
        super.configureUI();
        newLobbyButton.setVisible(false);
        newLobbyButton.setDisable(true);
    }


    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }


}
