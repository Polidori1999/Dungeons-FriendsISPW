package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.entity.*;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;
import it.uniroma2.marchidori.maininterface.exception.UserDataAccessException;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDAOFileSys implements UserDAO {

    private static final String BASE_DIR = "src/main/java/it/uniroma2/marchidori/maininterface/repository/";
    private static final String USERS_FILE_PATH = BASE_DIR + "users.txt";
    private static final String USER_DATA_DIR = BASE_DIR + "user/";
    private static final String CHARACTER_SHEET = "characterSheets.csv";
    private static final String JOINED_LOBBIES = "joinedLobbies.csv";
    private static final String FAVOURITE_LOBBIES = "favouriteLobbies.csv";

    private static final Logger logger = Logger.getLogger(UserDAOFileSys.class.getName());

    public UserDAOFileSys() {
        createDirectory();
    }

    //se non esiste la cartella relativa la crea
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

    //metodo per salvare un nuovo user nel fileSystem
    @Override
    public void saveUser(String email, String password) throws AccountAlreadyExistsException {

        if (userExists(email)) {
            throw new AccountAlreadyExistsException("Account already exists for email: " + email);
        }

        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH, true))) {
            writer.write(email + "," + hashedPassword);
            writer.newLine();
        } catch (IOException e) {
            throw new UserDataAccessException("Errore nella scrittura del file users.txt: " + e.getMessage(), e);
        }
        File userDir = getUserFolder(email);
        if (!userDir.exists()) {
            if (userDir.mkdirs()) {
                logger.log(Level.INFO, "Directory dei dati utente creata: {0}", email);
            } else {
                logger.log(Level.INFO, "Errore nella creazione della cartella per: {0}" , email);
            }
        }
        createUserDataFiles(userDir);
    }

    //metodo per vedere se un utente esiste già
    private boolean userExists(String email) {
        File file = new File(USERS_FILE_PATH);
        if (!file.exists()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new UserDataAccessException("Errore durante la verifica dell'esistenza dell'utente " + email, e);
        }
        return false;
    }

    //restituisce il folder relativo
    private static File getUserFolder(String email) {
        String mail = email.replaceAll("[^a-zA-Z0-9._@-]", "_");
        return new File(USER_DATA_DIR + mail);
    }

    //crea i file contenuti nelle cartelle
    private void createUserDataFiles(File userDir) {
        String[] fileNames = {CHARACTER_SHEET, JOINED_LOBBIES, FAVOURITE_LOBBIES};
        for (String fileName : fileNames) {
            File file = new File(userDir, fileName);
            if (!file.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.newLine();
                } catch (IOException e) {
                    logger.severe("Errore nella creazione del file " + file.getPath() + ": " + e.getMessage());
                }
            }
        }
    }

    //restituisce uno user dopo una ricerca tra quelli esistenti
    @Override
    public User getUserByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    return new User(data[0], data[1], new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file users.txt: " + e.getMessage());
        }
        return null;
    }

    //aggiorna i dati dello user
    public void updateUsersEntityData(User user) {
        File userDir = getUserFolder(user.getEmail());
        if (!userDir.exists() && !userDir.mkdirs()) {
            logger.severe("Errore nella creazione della cartella per: " + user.getEmail());
            return;
        }
        // Riscrive il file dei character sheets
        File characterFile = new File(userDir, CHARACTER_SHEET);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(characterFile, false))) {
            for (CharacterSheet cs : user.getCharacterSheets()) {
                writer.write(characterSheetToString(cs));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UserDataAccessException("Errore durante l'aggiornamento dei character sheets per l'utente " + user.getEmail(), e);
        }
        // Riscrive il file delle joined lobbies
        File joinedFile = new File(userDir, JOINED_LOBBIES);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(joinedFile, false))) {
            for (Lobby lobby : user.getJoinedLobbies()) {
                writer.write(lobbyToString(lobby));
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore durante l'aggiornamento delle joined lobbies: " + e.getMessage());
        }
        // Riscrive il file delle favourite lobbies
        File favFile = new File(userDir, FAVOURITE_LOBBIES);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(favFile, false))) {
            for (Lobby lobby : user.getFavouriteLobbies()) {
                writer.write(lobbyToString(lobby));
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore durante l'aggiornamento delle favourite lobbies: " + e.getMessage());
        }
    }

    //converte l entity in una stringa
    public String characterSheetToString(CharacterSheet cs) {
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
    //converte la stringa in un entity
    public Lobby stringToLobby(String line) {
        String[] parts = line.split(";", -1);
        String lobbyName = parts[0];
        String duration = parts[1];
        String type = parts[2];
        int numberOfPlayers = Integer.parseInt(parts[3]);
        String owner = parts[4];
        String infoLink = parts[5];
        int joinedPlayersCount = Integer.parseInt(parts[6]);
        return new Lobby(lobbyName, duration, type, numberOfPlayers, owner, infoLink, joinedPlayersCount);
    }

    //converte l entity in una stringa
    public String lobbyToString(Lobby lobby) {
        if (lobby == null) {
            return "Lobby is null";
        }
        String[] fields = new String[7];
        fields[0] = lobby.getLobbyName();
        fields[1] = lobby.getDuration();
        fields[2] = lobby.getLiveOnline();
        fields[3] = String.valueOf(lobby.getMaxOfPlayers());
        fields[4] = lobby.getOwner();
        fields[5] = lobby.getInfoLink();
        fields[6] = String.valueOf(lobby.getJoinedPlayersCount());
        return String.join(";", fields);
    }

    //preleva dai file i dati dell'utente
    public User loadUserData(User user) throws FileNotFoundException {
        File userDir = getUserFolder(user.getEmail());
        if (!userDir.exists() || !userDir.isDirectory()) {
            return null;
        }

        List<CharacterSheet> characterSheets = loadItems(
                new File(userDir, CHARACTER_SHEET),
                this::stringToCharacterSheet,
                "Errore nel caricamento dei character sheets per l'utente " + user.getEmail()
        );

        List<Lobby> joinedLobbies = loadItems(
                new File(userDir, JOINED_LOBBIES),
                this::stringToLobby,
                "Errore nel caricamento delle joined lobbies per l'utente " + user.getEmail()
        );

        List<Lobby> favouriteLobbies = loadItems(
                new File(userDir, FAVOURITE_LOBBIES),
                this::stringToLobby,
                "Errore nel caricamento delle favourite lobbies per l'utente " + user.getEmail()
        );

        String dummy = "******";
        return new User(user.getEmail(), dummy, characterSheets, favouriteLobbies, joinedLobbies);
    }


    //metodo di lettura e restituzione delle linee
    private <T> List<T> loadItems(File file, Function<String, T> deserializer, String errorMessage) {
        List<T> items = new ArrayList<>();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        items.add(deserializer.apply(line));
                    }
                }
            } catch (IOException e) {
                throw new UserDataAccessException(errorMessage, e);
            }
        }
        return items;
    }

    //converte una stringa in un entity
    public CharacterSheet stringToCharacterSheet(String line) {
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
}