package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;

import java.util.List;

public class ManageLobbyListPlayerBoundary extends ManageLobbyListBoundary {
    private final Jout jout = new Jout(this.getClass().getSimpleName());


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
        jout.print("✅ Evento ricevuto: la lista delle lobby è cambiata! Aggiorno la UI...");
        refreshTable();
    }

    @Override
    public void refreshTable() {

        
        jout.print("🔄 Aggiornamento UI della lista lobby...");
        jout.print("🎯 Prima del refresh, elementi nella tabella: " + tableViewLobby.getItems().size());

        tableViewLobby.getItems().clear();
        List<LobbyBean> updatedList = controller.getJoinedLobbies();
        jout.print("🔄 Nuova lista ricevuta dal controller: " + updatedList);

        tableViewLobby.getItems().addAll(updatedList);
        tableViewLobby.refresh();

        jout.print("🎯 Dopo il refresh, elementi nella tabella: " + tableViewLobby.getItems().size());
    }

}
