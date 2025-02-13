package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDAODatabase implements UserDAO {

    // Creazione del logger
    private static final Logger logger = Logger.getLogger(UserDAODatabase.class.getName());

    @Override
    public void saveUser(String email, String password) {
        // Verifica se l'utente esiste già
        if (getUserByEmail(email) != null) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe(String.format("❌ Errore: Email già registrata - %s", email));
            }
            return;
        }

        // Controllo se la password è già criptata (per evitare doppia cifratura)
        if (!password.startsWith("$2a$")) {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("🔍 DEBUG: Password hashata prima dell'inserimento → '%s'", password));
        }

        String query = "INSERT INTO users (email, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info(String.format("✅ Utente salvato nel database: %s", email));
                }
            } else {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.severe("❌ ERRORE: Nessuna riga inserita nel database!");
                }
            }
        } catch (SQLException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe(String.format("❌ Errore nel salvataggio dell'utente: %s", e.getMessage()));
            }
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String query = "SELECT email, password FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String foundEmail = rs.getString("email");
                String hashedPassword = rs.getString("password").trim();

                // Debug: log della password recuperata dal database
                if (logger.isLoggable(Level.INFO)) {
                    logger.info(String.format("🔍 DEBUG: Password dal database per %s → %s", foundEmail, hashedPassword));
                }

                return new User(
                        foundEmail,
                        hashedPassword,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                );
            } else {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning(String.format("❌ Utente non trovato nel database: %s", email));
                }
            }
        } catch (SQLException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe(String.format("❌ Errore nel recupero dell'utente dal database: %s", e.getMessage()));
            }
        }
        return null;
    }

    @Override
    public void saveUsersEntityData(User user) {

    }

    @Override
    public String serializeCharacterSheet(CharacterSheet cs) {
        return "";
    }

    @Override
    public String serializeLobby(Lobby lobby) {
        return "";
    }

    @Override
    public User loadUserData(User user) throws FileNotFoundException {
        return null;
    }

    @Override
    public void updateUsersEntityData(User user) {

    }


    public void removeUserLobby(String email, String lobby) {
        //vuoto
    }


    public List<String> getUserLobbies(String email) {
        return null;
    }


    public void saveUserLobbies(String email, List<String> lobbyNames) {
        //void
    }
}
