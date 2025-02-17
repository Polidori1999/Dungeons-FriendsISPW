package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JoinLobbyControllerTest {

    @Test
    void testEntityToBeanConversion_SingleLobby() {
        // Supponiamo che nella lobby siano joinati 2 giocatori
        int joinedCount = 2;
        Lobby lobby = new Lobby("TestLobby", "30", "Online", 4, "Smith", "https://test", joinedCount);
        List<Lobby> lobbyList = new ArrayList<>();
        lobbyList.add(lobby);


        List<LobbyBean> beans = Converter.convertLobbyListEntityToBean(lobbyList);

        assertNotNull(beans, "La lista dei LobbyBean non dovrebbe essere null.");
        assertEquals(1, beans.size(), "La lista dovrebbe contenere un solo LobbyBean.");

        LobbyBean bean = beans.get(0);
        assertEquals(lobby.getLobbyName(), bean.getName(), "Il nome della lobby deve corrispondere.");
        assertEquals(lobby.getDuration(), bean.getDuration(), "La durata della lobby deve corrispondere.");
        assertEquals(lobby.getLiveOnline(), bean.getLiveOnline(), "Il tipo (Live/Online) deve corrispondere.");
        assertEquals(lobby.getMaxOfPlayers(), bean.getMaxOfPlayers(), "Il numero massimo di giocatori deve corrispondere.");
        // Verifica che il contatore dei giocatori sia stato convertito correttamente
        assertEquals(lobby.getJoinedPlayersCount(), bean.getJoinedPlayersCount(), "Il contatore dei giocatori deve corrispondere.");
    }

    @Test
    void testEntityToBeanConversion_MultipleLobbies() {
        int joinedCount1 = 1;
        int joinedCount2 = 3;
        Lobby lobby1 = new Lobby("Lobby1", "30", "Online", 4, "prova1", "https://prova2", joinedCount1);
        Lobby lobby2 = new Lobby("Lobby2", "45", "Live", 6, "prova2", "https://prova2", joinedCount2);
        List<Lobby> lobbyList = new ArrayList<>();
        lobbyList.add(lobby1);
        lobbyList.add(lobby2);

        List<LobbyBean> beans = Converter.convertLobbyListEntityToBean(lobbyList);

        assertNotNull(beans, "La lista dei LobbyBean non dovrebbe essere null.");
        assertEquals(2, beans.size(), "La lista dovrebbe contenere due LobbyBean.");

        LobbyBean bean1 = beans.get(0);
        assertEquals(lobby1.getLobbyName(), bean1.getName(), "Il nome della prima lobby deve corrispondere.");
        assertEquals(lobby1.getDuration(), bean1.getDuration(), "La durata della prima lobby deve corrispondere.");
        assertEquals(lobby1.getLiveOnline(), bean1.getLiveOnline(), "Il tipo della prima lobby deve corrispondere.");
        assertEquals(lobby1.getMaxOfPlayers(), bean1.getMaxOfPlayers(), "Il numero massimo di giocatori della prima lobby deve corrispondere.");
        assertEquals(lobby1.getJoinedPlayersCount(), bean1.getJoinedPlayersCount(), "Il contatore dei giocatori della prima lobby deve corrispondere.");

        LobbyBean bean2 = beans.get(1);
        assertEquals(lobby2.getLobbyName(), bean2.getName(), "Il nome della seconda lobby deve corrispondere.");
        assertEquals(lobby2.getDuration(), bean2.getDuration(), "La durata della seconda lobby deve corrispondere.");
        assertEquals(lobby2.getLiveOnline(), bean2.getLiveOnline(), "Il tipo della seconda lobby deve corrispondere.");
        assertEquals(lobby2.getMaxOfPlayers(), bean2.getMaxOfPlayers(), "Il numero massimo di giocatori della seconda lobby deve corrispondere.");
        assertEquals(lobby2.getJoinedPlayersCount(), bean2.getJoinedPlayersCount(), "Il contatore dei giocatori della seconda lobby deve corrispondere.");
    }
}
