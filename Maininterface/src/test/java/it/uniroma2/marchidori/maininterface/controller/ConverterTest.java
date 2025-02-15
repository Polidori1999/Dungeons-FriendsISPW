package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    /**
     * Metodo da testare:
     * public static Lobby lobbyBeanToEntity(LobbyBean lobbyBean) { ... }
     */

    @Test
    @DisplayName("Test: Pass null come lobbyBean -> deve restituire null")
    void testLobbyBeanToEntity_NullInput() {
        Lobby result = Converter.lobbyBeanToEntity(null);
        assertNull(result, "Se l'input è null, il metodo dovrebbe restituire null.");
    }

    @Test
    @DisplayName("Test: Pass un bean con valori standard -> conversione corretta")
    void testLobbyBeanToEntity_ValidInput() {
        // Arrange
        LobbyBean bean = new LobbyBean(
                "TestLobby",    // name
                "Campagna",     // duration
                "Online",       // type
                5,              // numberOfPlayers
                "Owner",        // owner
                "http://info.link", // infoLink
                2               // joinedPlayersCount
        );

        // Act
        Lobby result = Converter.lobbyBeanToEntity(bean);

        // Assert
        assertNotNull(result, "Il risultato non dovrebbe essere null per un input valido.");
        assertEquals("TestLobby", result.getLobbyName());
        assertEquals("Campagna", result.getDuration());
        assertEquals("Online", result.getLiveOnline());
        assertEquals(5, result.getMaxOfPlayers());
        assertEquals("Owner", result.getOwner());
        assertEquals("http://info.link", result.getInfoLink());
        assertEquals(2, result.getJoinedPlayersCount());
    }

    @Test
    @DisplayName("Test: Pass un bean con joinedPlayersCount=0 -> conversione corretta")
    void testLobbyBeanToEntity_ZeroJoinedPlayers() {
        LobbyBean bean = new LobbyBean(
                "ZeroPlayersLobby",
                "Singola",
                "Online",
                5,
                "Owner2",
                "http://info.link2",
                0
        );

        Lobby result = Converter.lobbyBeanToEntity(bean);
        assertNotNull(result);
        assertEquals(0, result.getJoinedPlayersCount(),
                "joinedPlayersCount dovrebbe essere 0 se specificato così nel bean.");
    }

    @Test
    @DisplayName("Test: Pass un bean con joinedPlayersCount > maxOfPlayers (logica di business discutibile)")
    void testLobbyBeanToEntity_CountExceedingMax() {
        LobbyBean bean = new LobbyBean(
                "OvercrowdedLobby",
                "Campagna",
                "Presenza",
                3,  // maxOfPlayers
                "Owner3",
                "http://info.link3",
                5   // joinedPlayersCount > maxOfPlayers
        );

        Lobby result = Converter.lobbyBeanToEntity(bean);
        assertNotNull(result);
        assertEquals(5, result.getJoinedPlayersCount(),
                "Anche se joinedPlayersCount supera maxOfPlayers, il metodo deve mantenere il valore come da bean.");
        assertEquals(3, result.getMaxOfPlayers());
    }

    @Test
    @DisplayName("Test: Pass un bean con valori limite (maxOfPlayers=1, joinedPlayersCount=1)")
    void testLobbyBeanToEntity_EdgeValues() {
        LobbyBean bean = new LobbyBean(
                "EdgeLobby",
                "Singola",
                "Online",
                1,    // maxOfPlayers
                "OwnerEdge",
                "http://info.edge",
                1     // joinedPlayersCount
        );

        Lobby result = Converter.lobbyBeanToEntity(bean);
        assertNotNull(result);
        assertEquals(1, result.getMaxOfPlayers());
        assertEquals(1, result.getJoinedPlayersCount());
        assertTrue(result.isFull(),
                "Se joinedPlayersCount == maxOfPlayers, la lobby dovrebbe risultare piena (isFull == true).");
    }
}
