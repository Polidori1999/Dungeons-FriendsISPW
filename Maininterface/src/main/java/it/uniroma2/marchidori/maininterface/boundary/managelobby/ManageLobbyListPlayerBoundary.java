package it.uniroma2.marchidori.maininterface.boundary.managelobby;



public class ManageLobbyListPlayerBoundary extends ManageLobbyListBoundary {

    @Override
    protected void configureUI() {
        // Chiama la configurazione di base definita nella superclasse
        super.configureUI();
        newLobbyButton.setVisible(false);
        newLobbyButton.setDisable(true);
    }
}
