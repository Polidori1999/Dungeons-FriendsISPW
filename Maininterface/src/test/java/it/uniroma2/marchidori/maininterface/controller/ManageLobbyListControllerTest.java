package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static org.junit.jupiter.api.Assertions.*;

class ManageLobbyListControllerTest {

    private ManageLobbyListController controller;
    private UserBean testUserBean;
    private User testUser;

    /**
     * Metodo helper per creare un LobbyBean di test con un nome specifico.
     */
    private LobbyBean createTestLobbyBean(String name) {
        LobbyBean lobby = new LobbyBean("One-Shot", name, "Online", 4, false);
        lobby.setName(name);
        return lobby;
    }


    private Lobby createTestLobbyEntity(String name) {
        return new Lobby(name, "One-Shot", "Online", false, 4);
    }

    @BeforeEach
    void setUp() {
        testUserBean = new UserBean("Test","test",PLAYER,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());

        testUser = new User("", PLAYER, new ArrayList<CharacterSheet>(),new ArrayList<Lobby>(),new ArrayList<Lobby>());
        Session.getInstance().setCurrentUser(testUser);
        controller = new ManageLobbyListController();
        controller.setCurrentUser(testUserBean);
    }


    @Test
    void testDeleteLobby_ValidLobby() {
        LobbyBean lobbyBean = createTestLobbyBean("TestLobby");
        Lobby lobbyEntity = createTestLobbyEntity("TestLobby");

        testUserBean.getJoinedLobbies().add(lobbyBean);
        testUser.getJoinedLobbies().add(lobbyEntity);

        assertEquals(1, testUserBean.getJoinedLobbies().size());
        assertEquals(1, testUser.getJoinedLobbies().size());

        controller.deleteLobby("TestLobby");

        assertEquals(0, testUserBean.getJoinedLobbies().size(),
                "La lista dei LobbyBean dovrebbe essere vuota dopo l'eliminazione");
        assertEquals(0, testUser.getJoinedLobbies().size(),
                "La lista delle entity Lobby dovrebbe essere vuota dopo l'eliminazione");
    }

    @Test
    void testDeleteLobby_NonExistingLobby() {
        LobbyBean lobbyBean = createTestLobbyBean("ExistingLobby");
        Lobby lobbyEntity = createTestLobbyEntity("ExistingLobby");

        testUserBean.getJoinedLobbies().add(lobbyBean);
        testUser.getJoinedLobbies().add(lobbyEntity);

        assertEquals(1, testUserBean.getJoinedLobbies().size());
        assertEquals(1, testUser.getJoinedLobbies().size());

        controller.deleteLobby("NonExistingLobby");

        assertEquals(1, testUserBean.getJoinedLobbies().size(),
                "La lista dei LobbyBean non dovrebbe essere modificata se la lobby non viene trovata");
        assertEquals(1, testUser.getJoinedLobbies().size(),
                "La lista delle entity Lobby non dovrebbe essere modificata se la lobby non viene trovata");
    }



    @Test
    void testDeleteLobby_NullJoinedLobbies() {
        testUserBean.setJoinedLobbies(null);

        Lobby lobbyEntity = createTestLobbyEntity("TestLobby");
        testUser.getJoinedLobbies().add(lobbyEntity);

        controller.deleteLobby("TestLobby");

        assertEquals(1, testUser.getJoinedLobbies().size());
    }
}
