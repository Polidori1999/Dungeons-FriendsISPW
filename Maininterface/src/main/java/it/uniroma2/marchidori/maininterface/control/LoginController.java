package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    private UserService userService;
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    public LoginController() {
        this.userService = UserService.getInstance(false);
    }

    public void setCurrentUser(UserBean user) {
        // Metodo vuoto per implementare l'interfaccia
    }

    public User login(String email, String password) {
        logger.info("🔍 Tentativo di login per: " + email);

        User retrievedUser = userService.getUserByEmail(email);

        if (retrievedUser == null) {
            logger.severe("❌ Utente non trovato per: " + email);
            return null;
        }

        logger.info("🔄 Utente trovato: " + retrievedUser.getEmail());

        if (BCrypt.checkpw(password, retrievedUser.getPassword())) {
            logger.info("✅ Password corretta per: " + email);

            List<String> joinedLobbiesNames = userService.getUserLobbies(email);
            logger.info("📌 Lobby recuperate: " + joinedLobbiesNames);

            List<Lobby> joinedLobbies = new ArrayList<>();
            for (String lobbyName : joinedLobbiesNames) {
                Lobby lobby = Converter.stringToLobby(lobbyName);
                if (lobby != null) {
                    logger.info("🔄 Lobby convertita con successo: " + lobby.getLobbyName());
                    logger.info("📊 Dettagli lobby - Durata: " + lobby.getDuration() +
                            ", Tipo: " + lobby.getType() +
                            ", Proprietario: " + (lobby.isOwned() ? "Sì" : "No") +
                            ", Giocatori: " + lobby.getNumberOfPlayers());

                    joinedLobbies.add(lobby);
                } else {
                    logger.warning("⚠️ Impossibile convertire la lobby: " + lobbyName);
                }
            }

            retrievedUser.setJoinedLobbies(joinedLobbies);

            UserBean convertedUser = Converter.convert(retrievedUser);
            logger.info("🔄 Conversione User -> UserBean completata: " + convertedUser.getEmail());

            setCurrentUser(convertedUser);
            Session.getInstance().setCurrentUser(retrievedUser);

            return retrievedUser;
        } else {
            logger.severe("❌ Password errata per: " + email);
            return null;
        }
    }
}
