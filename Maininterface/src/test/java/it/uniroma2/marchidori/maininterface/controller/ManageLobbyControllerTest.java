package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageLobbyControllerTest {

    @Test
    void testFindLobbyByName_Found() {
        List<LobbyBean> lobbyList = new ArrayList<>();
        LobbyBean lobby1 = new LobbyBean("30", "Lobby1", "Online", 4, false);
        LobbyBean lobby2 = new LobbyBean("45", "Lobby2", "Live", 5, true);
        lobbyList.add(lobby1);
        lobbyList.add(lobby2);

        ManageLobbyController controller = new ManageLobbyController();
        LobbyBean foundLobby = controller.findLobbyByName("Lobby1", lobbyList);

        assertNotNull(foundLobby, "La lobby non dovrebbe essere null quando esiste.");
        assertEquals(lobby1.getName(), foundLobby.getName());
        assertEquals(lobby1.getDuration(), foundLobby.getDuration());
        assertEquals(lobby1.getLiveOnline(), foundLobby.getLiveOnline());
        assertEquals(lobby1.getNumberOfPlayers(), foundLobby.getNumberOfPlayers());
        assertEquals(lobby1.isOwned(), foundLobby.isOwned());
    }

    @Test
    void testFindLobbyByName_NotFound() {
        List<LobbyBean> lobbyList = new ArrayList<>();
        LobbyBean lobby1 = new LobbyBean("30", "Lobby1", "Online", 4, false);
        lobbyList.add(lobby1);

        ManageLobbyController controller = new ManageLobbyController();
        LobbyBean result = controller.findLobbyByName("NonExistingLobby", lobbyList);

        assertNull(result, "Il metodo dovrebbe restituire null se la lobby non esiste nella lista.");
    }


    @Test
    void testFindLobbyByName_EmptyList() {
        List<LobbyBean> emptyList = new ArrayList<>();
        ManageLobbyController controller = new ManageLobbyController();
        LobbyBean result = controller.findLobbyByName("AnyLobby", emptyList);

        assertNull(result, "Il metodo dovrebbe restituire null se la lista è vuota.");
    }


    @Test
    void testFindLobbyByName_NullList() {
        ManageLobbyController controller = new ManageLobbyController();
        LobbyBean result = controller.findLobbyByName("AnyLobby", null);

        assertNull(result, "Il metodo dovrebbe restituire null se la lista è null.");
    }


    @Test
    void testFindLobbyByName_CaseSensitivity() {
        List<LobbyBean> lobbyList = new ArrayList<>();
        LobbyBean lobby1 = new LobbyBean("30", "Lobby1", "Online", 4, false);
        lobbyList.add(lobby1);

        ManageLobbyController controller = new ManageLobbyController();
        LobbyBean result = controller.findLobbyByName("lobby1", lobbyList); // uso lettere minuscole

        assertNull(result, "Il metodo dovrebbe restituire null se il case non corrisponde.");
    }
}
