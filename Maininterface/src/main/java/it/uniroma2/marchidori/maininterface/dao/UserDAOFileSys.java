package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.entity.*;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;

public class UserDAOFileSys implements UserDAO {
    // Assicurati che i path terminino con "/" per concatenare correttamente
    private static final String BASE_DIR = "src/main/java/it/uniroma2/marchidori/maininterface/repository/";
    private static final String USERS_FILE_PATH = BASE_DIR + "users.txt";
    private static final String USER_DATA_DIR = BASE_DIR + "user/";

    private static final Logger logger = Logger.getLogger(UserDAOFileSys.class.getName());

    public UserDAOFileSys() {
        createDirectory();
    }

    private void createDirectory() {
        File baseDir = new File(BASE_DIR);
        if (!baseDir.exists()) {
            if (baseDir.mkdirs()) {
                logger.info("Cartella creata: " + BASE_DIR);
            } else {
                logger.severe("Errore nella creazione della cartella: " + BASE_DIR);
            }
        }
    }

    @Override
    public void saveUser(String email, String password) {
        // Hash della password (viene calcolato una sola volta)
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH, true))) {
            writer.write(email + "," + hashedPassword);
            writer.newLine();
        } catch (IOException e) {
            logger.severe("Errore nella scrittura del file users.txt: " + e.getMessage());
        }

        // Creazione della cartella dell'utente, se non esiste
        File userDir = getUserFolder(email);
        if (!userDir.exists()) {
            if (userDir.mkdirs()) {
                logger.info("Directory dei dati utente creata: " + email);
            } else {
                logger.severe("Errore nella creazione della cartella per: " + email);
            }
        }
        createUserDataFiles(userDir, email);
    }

    // Restituisce la cartella dell'utente, sanificando l'email
    private static File getUserFolder(String email) {
        String mail = email.replaceAll("[^a-zA-Z0-9._@-]", "_");
        return new File(USER_DATA_DIR + mail);
    }

    // Crea i file di dati dell'utente se non esistono già
    private void createUserDataFiles(File userDir, String email) {
        String[] fileNames = {"characterSheets.csv", "joinedLobbies.csv", "favouriteLobbies.csv"};
        for (String fileName : fileNames) {
            File file = new File(userDir, fileName);
            if (!file.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(email);
                    writer.newLine();
                } catch (IOException e) {
                    logger.severe("Errore nella creazione del file " + file.getPath() + ": " + e.getMessage());
                }
            }
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    // Restituisce un oggetto User con le informazioni basilari (le liste saranno poi aggiornate tramite loadUserData)
                    return new User(data[0], data[1], new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file users.txt: " + e.getMessage());
        }
        return null;
    }

    /**
     * Salva i dati dell'utente (entity) in file: characterSheets, joinedLobbies e favouriteLobbies.
     * Utilizza la modalità append per aggiungere nuove righe.
     */
    public void saveUsersEntityData(User user) {
        File userDir = getUserFolder(user.getEmail());
        if (!userDir.exists() && !userDir.mkdirs()) {
            logger.severe("Errore nella creazione della cartella per: " + user.getEmail());
            return;
        }

        // Salvataggio dei character sheets in modalità append
        File characterFile = new File(userDir, "characterSheets.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(characterFile, true))) {
            // Se il file è vuoto, scrive una riga di intestazione con l'email
            if (characterFile.length() == 0) {
                writer.write(user.getEmail());
                writer.newLine();
            }
            for (CharacterSheet cs : user.getCharacterSheets()) {
                writer.write(serializeCharacterSheet(cs));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Salvataggio delle joined lobbies in modalità append
        File joinedFile = new File(userDir, "joinedLobbies.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(joinedFile, true))) {
            if (joinedFile.length() == 0) {
                writer.write(user.getEmail());
                writer.newLine();
            }
            for (Lobby lobby : user.getJoinedLobbies()) {
                writer.write(serializeLobby(lobby));
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore durante il salvataggio delle joined lobbies: " + e.getMessage());
        }

        // Salvataggio delle favourite lobbies in modalità append
        File favFile = new File(userDir, "favouriteLobbies.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(favFile, true))) {
            if (favFile.length() == 0) {
                writer.write(user.getEmail());
                writer.newLine();
            }
            for (Lobby lobby : user.getFavouriteLobbies()) {
                writer.write(serializeLobby(lobby));
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore durante il salvataggio delle favourite lobbies: " + e.getMessage());
        }
    }

    public String serializeCharacterSheet(CharacterSheet cs) {
        CharacterInfo info = cs.getCharacterInfo();
        CharacterStats stats = cs.getCharacterStats();
        StringBuilder sb = new StringBuilder();
        sb.append(info.getName()).append(";")
                .append(info.getRace()).append(";")
                .append(info.getAge()).append(";")
                .append(info.getClasse()).append(";")
                .append(info.getLevel()).append(";")
                .append(stats.getStrength()).append(";")
                .append(stats.getDexterity()).append(";")
                .append(stats.getIntelligence()).append(";")
                .append(stats.getWisdom()).append(";")
                .append(stats.getCharisma()).append(";")
                .append(stats.getConstitution());
        return sb.toString();
    }

    /**
     * Deserializza una riga CSV in un oggetto Lobby.
     */
    public static Lobby deserializeLobby(String line) {
        String[] parts = line.split(";", -1);
        String lobbyName = parts[0];
        String duration = parts[1];
        String type = parts[2];
        boolean owned = Boolean.parseBoolean(parts[3]);
        int numberOfPlayers = Integer.parseInt(parts[4]);
        List<String> players = new ArrayList<>();
        if (parts.length > 5 && !parts[5].isEmpty()) {
            players = new ArrayList<>(Arrays.asList(parts[5].split("\\|")));
        }
        Lobby lobby = new Lobby(lobbyName, duration, type, owned, numberOfPlayers);
        lobby.getPlayers().addAll(players);
        return lobby;
    }

    /**
     * Deserializza una riga CSV in un oggetto CharacterSheet.
     */
    public static CharacterSheet deserializeCharacterSheet(String line) {
        String[] parts = line.split(";", -1);
        String name = parts[0];
        String race = parts[1];
        int age = Integer.parseInt(parts[2]);
        String classe = parts[3];
        int level = Integer.parseInt(parts[4]);
        int strength = Integer.parseInt(parts[5]);
        int dexterity = Integer.parseInt(parts[6]);
        int intelligence = Integer.parseInt(parts[7]);
        int wisdom = Integer.parseInt(parts[8]);
        int charisma = Integer.parseInt(parts[9]);
        int constitution = Integer.parseInt(parts[10]);
        CharacterInfo info = new CharacterInfo(name, race, age, classe, level);
        CharacterStats stats = new CharacterStats(strength, dexterity, intelligence, wisdom, charisma, constitution);
        return new CharacterSheet(info, stats);
    }

    public String serializeLobby(Lobby lobby) {
        StringBuilder sb = new StringBuilder();
        sb.append(lobby.getLobbyName()).append(";")
                .append(lobby.getDuration()).append(";")
                .append(lobby.getType()).append(";")
                .append(lobby.isOwned()).append(";")
                .append(lobby.getNumberOfPlayers()).append(";");
        List<String> players = lobby.getPlayers();
        if (players != null && !players.isEmpty()) {
            sb.append(String.join("|", players));
        }
        return sb.toString();
    }

    public User loadUserData(User user) throws FileNotFoundException {
        File userDir = getUserFolder(user.getEmail());
        if (!userDir.exists() || !userDir.isDirectory()) {
            return null;
        }

        // Carica i fogli personaggio
        File characterFile = new File(userDir, "characterSheets.csv");
        List<CharacterSheet> characterSheets = new ArrayList<>();
        if (characterFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(characterFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        CharacterSheet cs = deserializeCharacterSheet(line);
                        characterSheets.add(cs);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Carica le joined lobbies
        File joinedFile = new File(userDir, "joinedLobbies.csv");
        List<Lobby> joinedLobbies = new ArrayList<>();
        if (joinedFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(joinedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        Lobby lobby = deserializeLobby(line);
                        joinedLobbies.add(lobby);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Carica le favourite lobbies
        File favFile = new File(userDir, "favouriteLobbies.csv");
        List<Lobby> favouriteLobbies = new ArrayList<>();
        if (favFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(favFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        Lobby lobby = deserializeLobby(line);
                        favouriteLobbies.add(lobby);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // Impostiamo una password dummy; per il login la password viene recuperata da getUserByEmail
        String dummyPassword = "******";
        return new User(user.getEmail(), dummyPassword, characterSheets, favouriteLobbies, joinedLobbies);
    }
}
