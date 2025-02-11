package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;

import java.util.List;

public class ManageLobbyListPlayerBoundary extends ManageLobbyListBoundary {
    @Override
    protected void initialize() {
        // Chiama la configurazione di base definita nella superclasse
        super.initialize();
        LobbyRepository.addLobbyChangeListener(this);
        newLobbyButton.setVisible(false);
        newLobbyButton.setDisable(true);
    }

    @Override
    public void onLobbyListChanged() {
        System.out.println("✅ Evento ricevuto: la lista delle lobby è cambiata! Aggiorno la UI...");
        refreshTable();
    }


    public void refreshTable() {

        
        System.out.println("🔄 Aggiornamento UI della lista lobby...");
        System.out.println("🎯 Prima del refresh, elementi nella tabella: " + tableViewLobby.getItems().size());

        tableViewLobby.getItems().clear();
        List<LobbyBean> updatedList = controller.getJoinedLobbies();
        System.out.println("🔄 Nuova lista ricevuta dal controller: " + updatedList);

        tableViewLobby.getItems().addAll(updatedList);
        tableViewLobby.refresh();

        System.out.println("🎯 Dopo il refresh, elementi nella tabella: " + tableViewLobby.getItems().size());
    }

}
