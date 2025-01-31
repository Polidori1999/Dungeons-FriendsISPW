package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;

public class ManageLobbyListPlayerBoundary extends ManageLobbyListBoundary{

    private UserBean currentUser;

    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

}
