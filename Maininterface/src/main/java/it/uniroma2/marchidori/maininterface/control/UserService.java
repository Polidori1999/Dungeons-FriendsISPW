package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.LobbyDAOMem;
import it.uniroma2.marchidori.maininterface.dao.LobbyDaoFileSys;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private static UserService instance;
    private final UserDAO userDAO;

    private UserService(boolean useDatabase) {
        if (Session.getInstance().getDemo()) {
            this.userDAO = UserDAOFactory.getInstance().getUserDAO(useDatabase, Session.getInstance().getDemo());
        } else {
            this.userDAO = UserDAOFactory.getInstance().getUserDAO(useDatabase);
        }
    }

    public static UserService getInstance(boolean useDatabase) {
        if (instance == null) {
            instance = new UserService(useDatabase);
        }
        return instance;

    }

    public void registerUser(String email, String password) throws AccountAlreadyExistsException {
        userDAO.saveUser(email, password);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }


    public User loadUserData(User user) throws FileNotFoundException {
        Session.getInstance().setLobbyDAO(new LobbyDaoFileSys());
        Session.getInstance().setUserDAO(UserDAOFactory.getInstance().getUserDAO(Session.getInstance().getDB()));
        System.out.println("sto vedendo quale dao sto usando: "+ Session.getInstance().getDB());
        User loadedUser = Session.getInstance().getUserDAO().loadUserData(user);

        // Pulizia delle lobby obsolete
        cleanUserJoinedLobbies(loadedUser);

        Session.getInstance().getUserDAO().updateUsersEntityData(loadedUser);

        return loadedUser;

        //return Session.getInstance().getUserDAO().loadUserData(user);
    }


    public User loadUserDataDemo(String email){
        Session.getInstance().setUserDAO(userDAO);
        Session.getInstance().setLobbyDAO(LobbyDAOMem.getInstance());
        return getUserByEmail(email);
    }


    public void cleanUserJoinedLobbies(User user) {
        // Recupera il DAO delle lobby dalla sessione (assicurandoti che sia il DAO giusto, in questo caso quello da file)
        LobbyDaoFileSys lobbyDao = (LobbyDaoFileSys) Session.getInstance().getLobbyDAO();
        List<Lobby> activeLobbies = lobbyDao.getLobby();

        // Filtra le lobby joinate dell'utente, tenendo solo quelle che sono attive
        List<Lobby> filteredLobbies = user.getJoinedLobbies().stream()
                .filter(joined -> activeLobbies.stream()
                        .anyMatch(active -> active.getLobbyName().equals(joined.getLobbyName())))
                .collect(Collectors.toList());

        // Aggiorna la lista delle lobby dell'utente
        user.setJoinedLobbies(filteredLobbies);
    }
}
