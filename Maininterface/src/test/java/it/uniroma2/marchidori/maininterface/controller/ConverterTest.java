package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    ////////////////////////////////Edoardo Marchionni/////////////////////////////////////

    @Test
    @DisplayName("Test: Pass null come lobbyBean -> deve restituire null")
    void testLobbyBeanToEntity_NullInput() {
        //testo per conversione con bean nullo
        Lobby result = Converter.lobbyBeanToEntity(null);
        //controllo che la conversione sia ancora null
        assertNull(result, "Se l'input è null, il metodo dovrebbe restituire null.");
    }

    @Test
    @DisplayName("Test: Pass un bean con valori standard -> conversione corretta")
    void testLobbyBeanToEntity_ValidInput() {
        //bean di test
        LobbyBean bean = new LobbyBean(
                "TestLobby",    // name
                "Campagna",     // duration
                "Online",       // type
                5,              // numberOfPlayers
                "Owner",        // owner
                "http://info.link", // infoLink
                2               // joinedPlayersCount
        );

        //conversione
        Lobby result = Converter.lobbyBeanToEntity(bean);

        //controllo che la conversione sia non nulla e che tutti i campi siano convertiti corrrettamenti
        assertNotNull(result, "Il risultato non dovrebbe essere null per un input valido.");
        assertEquals("TestLobby", result.getLobbyName());
        assertEquals("Campagna", result.getDuration());
        assertEquals("Online", result.getLiveOnline());
        assertEquals(5, result.getMaxOfPlayers());
        assertEquals("Owner", result.getOwner());
        assertEquals("http://info.link", result.getInfoLink());
        assertEquals(2, result.getJoinedPlayersCount());
    }

    //test per la conversione corretta dei joined player count
    @Test
    @DisplayName("Test: Pass un bean con joinedPlayersCount=0 -> conversione corretta")
    void testLobbyBeanToEntity_ZeroJoinedPlayers() {
        //bean di test
        LobbyBean bean = new LobbyBean(
                "ZeroPlayersLobby",
                "Singola",
                "Online",
                5,
                "Owner2",
                "http://info.link2",
                0
        );

        //conversione
        Lobby result = Converter.lobbyBeanToEntity(bean);
        //controllo di conversione non nulla e se conversione di un campo corretta
        assertNotNull(result);
        assertEquals(0, result.getJoinedPlayersCount(),
                "joinedPlayersCount dovrebbe essere 0 se specificato così nel bean.");
    }


    //test per verificare la conversione di una lobby piena rimanga piena
    @Test
    @DisplayName("Test: Pass un bean con valori limite (maxOfPlayers=1, joinedPlayersCount=1)")
    void testLobbyBeanToEntity_EdgeValues() {
        //lobby bean di test
        LobbyBean bean = new LobbyBean(
                "EdgeLobby",
                "Singola",
                "Online",
                1,    // maxOfPlayers
                "OwnerEdge",
                "http://info.edge",
                1     // joinedPlayersCount
        );

        //il converter esegue la conversione
        Lobby result = Converter.lobbyBeanToEntity(bean);
        //asserti di controllo per risultato non nullo e se player correnti e massimi siano invariati
        assertNotNull(result);
        assertEquals(1, result.getMaxOfPlayers());
        assertEquals(1, result.getJoinedPlayersCount());
        assertTrue(result.isFull(),
                "Se joinedPlayersCount == maxOfPlayers, la lobby dovrebbe risultare piena (isFull == true).");
    }
}