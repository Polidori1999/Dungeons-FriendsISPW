package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.LobbyDAOMem;
import it.uniroma2.marchidori.maininterface.dao.LobbyDaoFileSys;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    // Istanza singleton di UserService
    private static UserService instance;
    private final UserDAO userDAO;

    private UserService(boolean useDatabase) {
        if (Session.getInstance().getDemo()) {
            //Se la Session indica "demo" = true, useremo il DAO in modalità demo
            this.userDAO = UserDAOFactory.getInstance().getUserDAO(useDatabase, Session.getInstance().getDemo());
        } else {
            this.userDAO = UserDAOFactory.getInstance().getUserDAO(useDatabase);
        }
    }


    //singleton
    public static UserService getInstance(boolean useDatabase) {
        if (instance == null) {
            instance = new UserService(useDatabase);
        }
        return instance;

    }

    //Registra un nuovo utente nel sistema, usando email e password.
    public void registerUser(String email, String password) throws AccountAlreadyExistsException {
        userDAO.saveUser(email, password);
    }

    //ecupera l'entità User in base alla email.
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }


    //Carica i dati completi di un utente e ripulisce da obsolete
    public User loadUserData(User user) throws FileNotFoundException {
        // Usa il LobbyDaoFileSys per le lobb
        Session.getInstance().setLobbyDAO(new LobbyDaoFileSys());
        Session.getInstance().setUserDAO(UserDAOFactory.getInstance().getUserDAO(Session.getInstance().getDB()));
        // Carica i dati completi dell'utente
        User loadedUser = Session.getInstance().getUserDAO().loadUserData(user);

        // Pulizia delle lobby obsolete
        cleanUserJoinedLobbies(loadedUser);
        //salvo i dati
        Session.getInstance().getUserDAO().updateUsersEntityData(loadedUser);

        return loadedUser;

    }

    //modalita demo
    public User loadUserDataDemo(String email){
        //recupero dal dao
        Session.getInstance().setUserDAO(userDAO);
        Session.getInstance().setLobbyDAO(LobbyDAOMem.getInstance());

        User loadedUser = getUserByEmail(email);

        // Pulisce le lobby joinate obsolete
        cleanUserJoinedLobbies(loadedUser);

        return loadedUser;
    }


    public void cleanUserJoinedLobbies(User user) {
        // Recupera il DAO delle lobby dalla sessione
        LobbyDAO lobbyDao = Session.getInstance().getLobbyDAO();
        List<Lobby> activeLobbies = lobbyDao.getLobby();

        // Filtra le lobby joinate dell'utente, tenendo solo quelle che sono attive
        List<Lobby> filteredLobbies = new ArrayList<>(user.getJoinedLobbies().stream()
                .filter(joined -> activeLobbies.stream()
                        .anyMatch(active->active.getLobbyName().equals(joined.getLobbyName()))).toList());

        // Aggiorna la lista delle lobby dell'utente
        user.setJoinedLobbies(filteredLobbies);
    }
}
