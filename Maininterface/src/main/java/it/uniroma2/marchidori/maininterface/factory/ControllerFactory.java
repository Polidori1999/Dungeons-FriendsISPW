package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;

public class ControllerFactory {
    private ControllerFactory() {
        //no instanziazione diretta
    }

    public static JoinLobbyController createJoinLobbyController() {
        return new JoinLobbyController();
    }



}
