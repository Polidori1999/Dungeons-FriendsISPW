package it.uniroma2.marchidori.maininterface.boundary.managelobby;



public class ManageLobbyListPlayerBoundary extends ManageLobbyListBoundary {


    @Override
    protected void initialize() {
        // Chiama la configurazione di base definita nella superclasse
        super.initialize();
        newLobbyButton.setVisible(false);
        newLobbyButton.setDisable(true);
    }
}
