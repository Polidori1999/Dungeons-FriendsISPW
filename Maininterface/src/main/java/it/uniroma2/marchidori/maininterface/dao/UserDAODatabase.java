package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.entity.*;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;
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
    public void saveUser(String email, String password) throws AccountAlreadyExistsException {
        // Verifica se l'utente esiste già
        if (getUserByEmail(email) != null) {
            throw new AccountAlreadyExistsException("Account already exists for email: " + email);
        }

        // Controllo se la password è già criptata (per evitare doppia cifratura)
        if (!password.startsWith("$2a$")) {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
        }

        String query = "INSERT INTO users (email, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.log(Level.INFO, "Utente salvato nel database: {0}", email);
            } else {
                logger.severe("ERRORE: Nessuna riga inserita nel database!");
            }
        } catch (SQLException e) {
            logger.severe(String.format("Errore nel salvataggio dell'utente: %s", e.getMessage()));
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


                return new User(
                        foundEmail,
                        hashedPassword,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                );
            } else {
                logger.log(Level.WARNING, "Utente non trovato nel database: {0}", email);
            }
        } catch (SQLException e) {
            logger.severe(String.format("Errore nel recupero dell'utente dal database: %s", e.getMessage()));
        }
        return null;
    }

    /**
     * Aggiorna nel database i dati associati all'utente: character sheets, joined lobbies e favourite lobbies.
     */
    @Override
    public void updateUsersEntityData(User user) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Disabilita l'autocommit per poter effettuare tutte le operazioni in un'unica transazione
            conn.setAutoCommit(false);

            // Elimina i vecchi dati dell'utente
            try (PreparedStatement deleteCS = conn.prepareStatement("DELETE FROM character_sheets WHERE user_email = ?")) {
                deleteCS.setString(1, user.getEmail());
                deleteCS.executeUpdate();
            }
            try (PreparedStatement deleteJL = conn.prepareStatement("DELETE FROM joined_lobbies WHERE user_email = ?")) {
                deleteJL.setString(1, user.getEmail());
                deleteJL.executeUpdate();
            }
            try (PreparedStatement deleteFL = conn.prepareStatement("DELETE FROM favourite_lobbies WHERE user_email = ?")) {
                deleteFL.setString(1, user.getEmail());
                deleteFL.executeUpdate();
            }

            // Inserisce i nuovi character sheets
            String insertCSQuery = "INSERT INTO character_sheets (user_email, name, race, age, class, level, strength, dexterity, intelligence, wisdom, charisma, constitution) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertCS = conn.prepareStatement(insertCSQuery)) {
                for (CharacterSheet cs : user.getCharacterSheets()) {
                    CharacterInfo info = cs.getCharacterInfo();
                    CharacterStats stats = cs.getCharacterStats();
                    insertCS.setString(1, user.getEmail());
                    insertCS.setString(2, info.getName());
                    insertCS.setString(3, info.getRace());
                    insertCS.setInt(4, info.getAge());
                    insertCS.setString(5, info.getClasse());  // Nota: se il campo nel DB si chiama "class" attenzione alle parole riservate!
                    insertCS.setInt(6, info.getLevel());
                    insertCS.setInt(7, stats.getStrength());
                    insertCS.setInt(8, stats.getDexterity());
                    insertCS.setInt(9, stats.getIntelligence());
                    insertCS.setInt(10, stats.getWisdom());
                    insertCS.setInt(11, stats.getCharisma());
                    insertCS.setInt(12, stats.getConstitution());
                    insertCS.addBatch();
                }
                insertCS.executeBatch();
            }

            // Inserisce le joined lobbies
            String insertJLQuery = "INSERT INTO joined_lobbies (user_email, lobby_name, duration, live_online, max_players, owner, info_link, joined_players_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertJL = conn.prepareStatement(insertJLQuery)) {
                for (Lobby lobby : user.getJoinedLobbies()) {
                    insertJL.setString(1, user.getEmail());
                    insertJL.setString(2, lobby.getLobbyName());
                    insertJL.setString(3, lobby.getDuration());
                    insertJL.setString(4, lobby.getLiveOnline());
                    insertJL.setInt(5, lobby.getMaxOfPlayers());
                    insertJL.setString(6, lobby.getOwner());
                    insertJL.setString(7, lobby.getInfoLink());
                    insertJL.setInt(8, lobby.getJoinedPlayersCount());
                    insertJL.addBatch();
                }
                insertJL.executeBatch();
            }

            // Inserisce le favourite lobbies
            String insertFLQuery = "INSERT INTO favourite_lobbies (user_email, lobby_name, duration, live_online, max_players, owner, info_link, joined_players_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertFL = conn.prepareStatement(insertFLQuery)) {
                for (Lobby lobby : user.getFavouriteLobbies()) {
                    insertFL.setString(1, user.getEmail());
                    insertFL.setString(2, lobby.getLobbyName());
                    insertFL.setString(3, lobby.getDuration());
                    insertFL.setString(4, lobby.getLiveOnline());
                    insertFL.setInt(5, lobby.getMaxOfPlayers());
                    insertFL.setString(6, lobby.getOwner());
                    insertFL.setString(7, lobby.getInfoLink());
                    insertFL.setInt(8, lobby.getJoinedPlayersCount());
                    insertFL.addBatch();
                }
                insertFL.executeBatch();
            }

            // Commit della transazione
            conn.commit();
            logger.log(Level.INFO, "Dati utente aggiornati per: {0}", user.getEmail());
        } catch (SQLException e) {
            logger.severe("Errore nell'aggiornamento dei dati utente: " + e.getMessage());
        }
    }

    /**
     * Carica dal database i dati associati all'utente e li inserisce nell'oggetto User.
     */
    @Override
    public User loadUserData(User user) throws FileNotFoundException {
        List<CharacterSheet> characterSheets = new ArrayList<>();
        List<Lobby> joinedLobbies = new ArrayList<>();
        List<Lobby> favouriteLobbies = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Carica i character sheets
            String queryCS = "SELECT name, race, age, class, level, strength, dexterity, intelligence, wisdom, charisma, constitution FROM character_sheets WHERE user_email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryCS)) {
                stmt.setString(1, user.getEmail());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String name = rs.getString("name");
                    String race = rs.getString("race");
                    int age = rs.getInt("age");
                    String classe = rs.getString("class");
                    int level = rs.getInt("level");
                    int strength = rs.getInt("strength");
                    int dexterity = rs.getInt("dexterity");
                    int intelligence = rs.getInt("intelligence");
                    int wisdom = rs.getInt("wisdom");
                    int charisma = rs.getInt("charisma");
                    int constitution = rs.getInt("constitution");

                    CharacterInfo info = new CharacterInfo(name, race, age, classe, level);
                    CharacterStats stats = new CharacterStats(strength, dexterity, intelligence, wisdom, charisma, constitution);
                    characterSheets.add(new CharacterSheet(info, stats));
                }
            }

            // Carica le joined lobbies
            String queryJL = "SELECT lobby_name, duration, live_online, max_players, owner, info_link, joined_players_count FROM joined_lobbies WHERE user_email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryJL)) {
                stmt.setString(1, user.getEmail());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String lobbyName = rs.getString("lobby_name");
                    String duration = rs.getString("duration");
                    String liveOnline = rs.getString("live_online");
                    int maxPlayers = rs.getInt("max_players");
                    String owner = rs.getString("owner");
                    String infoLink = rs.getString("info_link");
                    int joinedPlayersCount = rs.getInt("joined_players_count");

                    Lobby lobby = new Lobby(lobbyName, duration, liveOnline, maxPlayers, owner, infoLink, joinedPlayersCount);
                    joinedLobbies.add(lobby);
                }
            }

            // Carica le favourite lobbies
            String queryFL = "SELECT lobby_name, duration, live_online, max_players, owner, info_link, joined_players_count FROM favourite_lobbies WHERE user_email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryFL)) {
                stmt.setString(1, user.getEmail());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String lobbyName = rs.getString("lobby_name");
                    String duration = rs.getString("duration");
                    String liveOnline = rs.getString("live_online");
                    int maxPlayers = rs.getInt("max_players");
                    String owner = rs.getString("owner");
                    String infoLink = rs.getString("info_link");
                    int joinedPlayersCount = rs.getInt("joined_players_count");

                    Lobby lobby = new Lobby(lobbyName, duration, liveOnline, maxPlayers, owner, infoLink, joinedPlayersCount);
                    favouriteLobbies.add(lobby);
                }
            }
        } catch (SQLException e) {
            logger.severe("Errore nel caricamento dei dati utente: " + e.getMessage());
        }
        // Restituisce l'utente con i dati caricati (la password viene impostata a un dummy per sicurezza)
        return new User(user.getEmail(), "******", characterSheets, favouriteLobbies, joinedLobbies);
    }
}