package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageLobbyControllerTest {

    ////////////////////////////////Leonardo Polidori/////////////////////////////////////


    @Test
    void testFindLobbyByName_Found() {
        List<LobbyBean> lobbyList = new ArrayList<>();
        LobbyBean lobby1 = new LobbyBean("Lobby1", "One-shot", "Online", 4, "prova1","htpps://prova1",2);
        LobbyBean lobby2 = new LobbyBean("lobby2", "Campaign", "Live", 5, "prova2","https://prova2",3);
        lobbyList.add(lobby1);
        lobbyList.add(lobby2);

        ManageLobbyController controller = new ManageLobbyController();
        LobbyBean foundLobby = controller.findLobbyByName("Lobby1", lobbyList);

        assertNotNull(foundLobby, "La lobby non dovrebbe essere null quando esiste.");
        assertEquals(lobby1.getName(), foundLobby.getName());
        assertEquals(lobby1.getDuration(), foundLobby.getDuration());
        assertEquals(lobby1.getLiveOnline(), foundLobby.getLiveOnline());
        assertEquals(lobby1.getMaxOfPlayers(), foundLobby.getMaxOfPlayers());
        assertEquals(lobby1.getOwner(), foundLobby.getOwner());
        assertEquals(lobby1.getJoinedPlayersCount(), foundLobby.getJoinedPlayersCount());
    }

    @Test
    void testFindLobbyByName_NotFound() {
        List<LobbyBean> lobbyList = new ArrayList<>();
        LobbyBean lobby1 = new LobbyBean("lobby1", "One-shot", "Online", 4, "prova1","htpps://prova1",2);
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
        LobbyBean lobby1 = new LobbyBean("Lobby1", "One-shot", "Online", 4, "prova1","htpps://prova1",2);
        lobbyList.add(lobby1);

        ManageLobbyController controller = new ManageLobbyController();
        LobbyBean result = controller.findLobbyByName("lobby1", lobbyList); // uso lettere minuscole

        assertNull(result, "Il metodo dovrebbe restituire null se il case non corrisponde.");
    }
}
