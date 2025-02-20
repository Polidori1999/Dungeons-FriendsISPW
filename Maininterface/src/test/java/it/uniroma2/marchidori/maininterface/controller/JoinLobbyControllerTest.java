package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.dao.LobbyDAOMem;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JoinLobbyControllerTest {

    private JoinLobbyController joinLobbyController;
    private UserBean currentUserBean;
    private User currentUser;
    private LobbyBean lobbyBean;
    private LobbyDAOMem lobbyDAOMem;

    //////////////////////EDOARDO MARCHIONNI///////////////////////////////////

    @BeforeEach
    void setUp() {
        Session.getInstance().setLobbyDAO(lobbyDAOMem);
        Session.getInstance().setUserDAO(UserDAOFactory.getInstance().getUserDAO(false, true));
        currentUserBean = new UserBean("test@example.com", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        currentUser = Converter.userBeanToEntity(currentUserBean);
        Session.getInstance().setCurrentUser(currentUser);
        joinLobbyController = new JoinLobbyController();
        joinLobbyController.setCurrentUser(currentUserBean);
        lobbyBean = new LobbyBean("TestLobby","Campaign","Live",6,"test","link",4);
    }

    //test per verificare se si aggiunge effettivamente una lobby ai preferiti
    @Test
    void testAddLobbyToFavourite() {
        currentUser.getFavouriteLobbies().clear();
        currentUserBean.getFavouriteLobbies().clear();
        assertTrue(currentUserBean.getFavouriteLobbies().isEmpty(), "Favorites list should be empty initially");
        assertTrue(currentUser.getFavouriteLobbies().isEmpty(), "Favorites list should be empty initially");

        joinLobbyController.addLobbyToFavourite(lobbyBean);

        assertEquals(1, currentUserBean.getFavouriteLobbies().size(), "Favorites list should contain one lobby");
        assertEquals(1, currentUser.getFavouriteLobbies().size(), "Favorites list should contain one lobby");
    }

    //test per verificare che non si aggiunga più volte la stessa lobby ai preferiti
    @Test
    void testAddSameLobbyToFavourite() {
        // Add the lobby to the favorites first time
        joinLobbyController.addLobbyToFavourite(lobbyBean);
        int initialSize = currentUserBean.getFavouriteLobbies().size();
        int initialSize2 = currentUser.getFavouriteLobbies().size();

        // Try to add the same lobby again
        joinLobbyController.addLobbyToFavourite(lobbyBean);

        // The list should not grow, meaning the same lobby is not added again
        assertEquals(initialSize, currentUserBean.getFavouriteLobbies().size(), "Duplicate lobby should not be added to favorites");
        assertEquals(initialSize2, currentUser.getFavouriteLobbies().size(), "Duplicate lobby should not be added to favorites");
    }
}