package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JoinLobbyControllerTest {

    @Test
    void testEntityToBeanConversion_SingleLobby() {
        Lobby lobby = new Lobby("TestLobby", "30", "Online", false, 5);
        List<Lobby> lobbyList = new ArrayList<>();
        lobbyList.add(lobby);

        JoinLobbyController controller = new JoinLobbyController();
        List<LobbyBean> beans = controller.getList(lobbyList);

        assertNotNull(beans, "La lista dei LobbyBean non dovrebbe essere null.");
        assertEquals(1, beans.size(), "La lista dovrebbe contenere un solo LobbyBean.");

        LobbyBean bean = beans.get(0);
        assertEquals(lobby.getLobbyName(), bean.getName(), "Il nome della lobby deve corrispondere.");
        assertEquals(lobby.getDuration(), bean.getDuration(), "La durata della lobby deve corrispondere.");
        assertEquals(lobby.getType(), bean.getLiveOnline(), "Il tipo (Live/Online) deve corrispondere.");
        assertEquals(lobby.getNumberOfPlayers(), bean.getNumberOfPlayers(), "Il numero di giocatori deve corrispondere.");
    }

    @Test
    void testEntityToBeanConversion_MultipleLobbies() {
        Lobby lobby1 = new Lobby("Lobby1", "30", "Online", false, 4);
        Lobby lobby2 = new Lobby("Lobby2", "45", "Live", true, 6);
        List<Lobby> lobbyList = new ArrayList<>();
        lobbyList.add(lobby1);
        lobbyList.add(lobby2);

        JoinLobbyController controller = new JoinLobbyController();
        List<LobbyBean> beans = controller.getList(lobbyList);

        assertNotNull(beans, "La lista dei LobbyBean non dovrebbe essere null.");
        assertEquals(2, beans.size(), "La lista dovrebbe contenere due LobbyBean.");

        LobbyBean bean1 = beans.get(0);
        assertEquals(lobby1.getLobbyName(), bean1.getName(), "Il nome della prima lobby deve corrispondere.");
        assertEquals(lobby1.getDuration(), bean1.getDuration(), "La durata della prima lobby deve corrispondere.");
        assertEquals(lobby1.getType(), bean1.getLiveOnline(), "Il tipo della prima lobby deve corrispondere.");
        assertEquals(lobby1.getNumberOfPlayers(), bean1.getNumberOfPlayers(), "Il numero di giocatori della prima lobby deve corrispondere.");

        LobbyBean bean2 = beans.get(1);
        assertEquals(lobby2.getLobbyName(), bean2.getName(), "Il nome della seconda lobby deve corrispondere.");
        assertEquals(lobby2.getDuration(), bean2.getDuration(), "La durata della seconda lobby deve corrispondere.");
        assertEquals(lobby2.getType(), bean2.getLiveOnline(), "Il tipo della seconda lobby deve corrispondere.");
        assertEquals(lobby2.getNumberOfPlayers(), bean2.getNumberOfPlayers(), "Il numero di giocatori della seconda lobby deve corrispondere.");
    }
}
