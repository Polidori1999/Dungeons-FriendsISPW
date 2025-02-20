package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;


import it.uniroma2.marchidori.maininterface.dao.LobbyDaoFileSys;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileNotFoundException;



public class LoginController implements UserAwareInterface {

    private UserService userService;

    UserBean currentUser;

    public LoginController() {
        this.userService = UserService.getInstance(Session.getInstance().getDB());
    }

    // Metodo dell'interfaccia UserAwareInterface
    public void setCurrentUser(UserBean userBean) {
        this.currentUser = userBean;
    }

    public User login(String email, String password) throws FileNotFoundException {


        // Recupera l'utente tramite il userservice
        User retrievedUser = userService.getUserByEmail(email);

        if (retrievedUser == null) {
            return null;
        }


        // Verifica la password usando BCrypt
        if (BCrypt.checkpw(password, retrievedUser.getPassword())) {
            if(Session.getInstance().getDemo()){

                retrievedUser = userService.loadUserDataDemo(email);
            }else{

                retrievedUser = userService.loadUserData(retrievedUser);
            }


            retrievedUser.setRoleBehavior(RoleEnum.PLAYER);
            // Converte l'entity User in un UserBean per l'interfaccia utente
            UserBean convertedUser = Converter.convert(retrievedUser);

            // Imposta l'utente corrente (sia a livello di controller che di sessione)
            setCurrentUser(convertedUser);
            Session.getInstance().setCurrentUser(retrievedUser);

            return retrievedUser;
        } else {
            return null;
        }
    }

    public void caseGuest(){
        //Imposta UserDAO per la demo
        Session.getInstance().setUserDAO(UserDAOFactory.getInstance().getUserDAO(false,true));
        // Usa LobbyDaoFileSys come DAO per le Lobby
        Session.getInstance().setLobbyDAO(new LobbyDaoFileSys());
    }
}
