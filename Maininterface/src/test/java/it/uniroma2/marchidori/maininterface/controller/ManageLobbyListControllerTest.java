package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.dao.LobbyDaoFileSys;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static org.junit.jupiter.api.Assertions.*;

class ManageLobbyListControllerTest {

    private ManageLobbyListController controller;
    private UserBean testUserBean;
    private User testUser;

    // Costanti per i parametri predefiniti delle lobby
    private static final String DEFAULT_TYPE = "One-shot";
    private static final String DEFAULT_MODE = "Online";
    private static final int DEFAULT_MAX_PLAYERS = 4;
    private static final String DEFAULT_FIELD1 = "prova1";
    private static final String DEFAULT_FIELD2 = "htpps://prova1";

    /**
     * Metodo helper per creare un LobbyBean di test con un nome specifico.
     */
    private LobbyBean createTestLobbyBean(String name) {
        LobbyBean lobby = new LobbyBean("lobby1", DEFAULT_TYPE, DEFAULT_MODE, DEFAULT_MAX_PLAYERS, DEFAULT_FIELD1, DEFAULT_FIELD2, 3);
        lobby.setName(name);
        return lobby;
    }

    /**
     * Metodo helper per creare una Lobby entity di test con un nome specifico.
     */
    private Lobby createTestLobbyEntity(String name) {
        return new Lobby(name, DEFAULT_TYPE, DEFAULT_MODE, DEFAULT_MAX_PLAYERS, DEFAULT_FIELD1, DEFAULT_FIELD2,3 );
    }

    /**
     * Metodo helper per aggiungere una lobby sia al bean che all'entity dell'utente.
     */
    private void addLobbyToUser(String name) {
        testUserBean.getJoinedLobbies().add(createTestLobbyBean(name));
        testUser.getJoinedLobbies().add(createTestLobbyEntity(name));
    }

    @BeforeEach
    void setUp() {
        testUserBean = new UserBean("Test", "test", PLAYER, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        testUser = new User("", PLAYER, new ArrayList<CharacterSheet>(), new ArrayList<Lobby>(), new ArrayList<Lobby>());
        Session.getInstance().setCurrentUser(testUser);
        controller = new ManageLobbyListController();
        controller.setCurrentUser(testUserBean);
    }

    @Test
    void testDeleteLobby_ValidLobby() throws IOException {
        // Aggiungi una lobby con nome "TestLobby" sia nel bean che nell'entity
        addLobbyToUser("TestLobby");

        assertEquals(1, testUserBean.getJoinedLobbies().size());
        assertEquals(1, testUser.getJoinedLobbies().size());
        LobbyDaoFileSys lobbyDao = new LobbyDaoFileSys();
        lobbyDao.addLobby(Converter.lobbyBeanToEntity(createTestLobbyBean("TestLobby")));
        // Passa una lobby bean con lo stesso nome per l'eliminazione
        controller.leaveLobby(createTestLobbyBean("TestLobby"));

        assertEquals(0, testUserBean.getJoinedLobbies().size(),
                "La lista dei LobbyBean dovrebbe essere vuota dopo l'eliminazione");
        assertEquals(0, testUser.getJoinedLobbies().size(),
                "La lista delle entity Lobby dovrebbe essere vuota dopo l'eliminazione");
    }

    @Test
    void testDeleteLobby_NonExistingLobby() throws IOException {
        // Aggiungi una lobby con nome "ExistingLobby"
        addLobbyToUser("ExistingLobby");

        assertEquals(1, testUserBean.getJoinedLobbies().size());
        assertEquals(1, testUser.getJoinedLobbies().size());

        LobbyDaoFileSys lobbyDao = new LobbyDaoFileSys();
        lobbyDao.addLobby(Converter.lobbyBeanToEntity(createTestLobbyBean("TestLobby")));

        // Prova a eliminare una lobby con nome "NonExistingLobby"
        LobbyBean lobbyToDelete = createTestLobbyBean("NonExistingLobby");
        controller.leaveLobby(lobbyToDelete);

        assertEquals(1, testUserBean.getJoinedLobbies().size(),
                "La lista dei LobbyBean non dovrebbe essere modificata se la lobby non viene trovata");
        assertEquals(1, testUser.getJoinedLobbies().size(),
                "La lista delle entity Lobby non dovrebbe essere modificata se la lobby non viene trovata");
    }

    @Test
    void testDeleteLobby_NullJoinedLobbies() throws IOException {
        testUserBean.setJoinedLobbies(null);

        testUser.getJoinedLobbies().add(createTestLobbyEntity("TestLobby"));
        LobbyDaoFileSys lobbyDao = new LobbyDaoFileSys();
        lobbyDao.addLobby(Converter.lobbyBeanToEntity(createTestLobbyBean("TestLobby")));

        controller.leaveLobby(Converter.lobbyEntityToBean(createTestLobbyEntity("TestLobby")));

        assertEquals(1, testUser.getJoinedLobbies().size(),
                "La lista delle entity Lobby dovrebbe rimanere invariata se la lista del UserBean è null");
    }
}
