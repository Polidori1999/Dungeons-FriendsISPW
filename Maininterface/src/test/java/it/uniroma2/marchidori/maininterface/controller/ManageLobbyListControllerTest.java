package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.dao.LobbyDAOMem;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

////////////////////////////////Leonardo Polidori/////////////////////////////////////



class ManageLobbyListControllerTest {

    private ManageLobbyListController controllerUnderTest;
    private User testUser;        // L'utente "entity" in Session
    private UserBean testUserBean; // La controparte Bean

    @BeforeEach
    void setUp() {
        // Pulizia sessione
        Session.getInstance().clear();

        // Inizializziamo un utente di prova
        testUser = new User(
                "user@test.it",
                "hashedPassword",
                new ArrayList<>(), // joinedLobbies
                new ArrayList<>(), // gmLobbies
                new ArrayList<>()  // friends
        );

        // La versione bean dell’utente
        testUserBean = new UserBean("user@test.it",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );


        // Settiamo l’utente in sessione
        Session.getInstance().setCurrentUser(testUser);
        Session.getInstance().setLobbyDAO(LobbyDAOMem.getInstance());

        // Istanziamo il controller e gli settiamo il currentUserBean
        controllerUnderTest = new ManageLobbyListController();
        controllerUnderTest.setCurrentUser(testUserBean);
    }


    @Test
    void testGetJoinedLobbies_Empty() {
        List<LobbyBean> result = controllerUnderTest.getJoinedLobbies();
        assertNotNull(result);
        assertTrue(result.isEmpty(), "La lista di lobby dovrebbe essere vuota se l'utente non ne ha nessuna.");
    }


    @Test
    void testGetJoinedLobbies_SomeLobbies() {
        // Aggiungiamo qualche lobby all'utente e aggiorniamo il bean
        Lobby lobbyA = new Lobby("LobbyA", "Singola", "Online", 4, "owner1", "http://infoA", 2);
        Lobby lobbyB = new Lobby("LobbyB", "Campagna", "Live", 5, "owner2", "http://infoB", 3);
        testUser.getJoinedLobbies().add(lobbyA);
        testUser.getJoinedLobbies().add(lobbyB);

        // Chiamiamo il metodo
        List<LobbyBean> result = controllerUnderTest.getJoinedLobbies();
        assertEquals(2, result.size(), "Dovrebbero esserci 2 lobby nella lista.");
        assertEquals("LobbyA", result.get(0).getName());
        assertEquals("LobbyB", result.get(1).getName());
    }


    @Test
    void testLeaveLobby_LobbyNotFound(){
        // Non creiamo nessuna lobby nel DAO
        // L'utente ha un bean per "LobbyNonEsistente" ma in DAO non c'è
        LobbyBean nonexistentLobbyBean = new LobbyBean("LobbyNonEsistente", "Singola", "Online",
                5, "someOwner", "http://info", 3);
        testUserBean.setJoinedLobbies(List.of(nonexistentLobbyBean));

        // L'utente entity però non ce l'ha (opzionale)

        // Invocazione
        controllerUnderTest.leaveLobby(nonexistentLobbyBean);

        // Non deve essere sollevata eccezione né deve succedere nulla.
        // L'utente bean rimane invariato
        assertEquals(1, testUserBean.getJoinedLobbies().size());
    }
}