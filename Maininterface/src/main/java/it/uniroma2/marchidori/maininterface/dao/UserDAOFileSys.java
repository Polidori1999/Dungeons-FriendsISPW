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
    private static final String BASE_DIR = "src/main/java/it/uniroma2/marchidori/maininterface/repository/";
    private static final String USERS_FILE_PATH = BASE_DIR + "users.txt";
    private static final String USER_DATA_DIR = BASE_DIR + "useser ";

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
                logger.severe("Errore nella creazione della cartella.");
            }
        }
    }


    @Override
    public void saveUser(String email, String password) {
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());//da spostare nel controller
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH, true))) {
            writer.write(email + "," + hashedPassword);
            writer.newLine();
        } catch (IOException e) {
            logger.severe("Errore nella scrittura del file: " + e.getMessage());
        }
        File userDir = getUserFolder(email);
        if (!userDir.exists()) {
            if (userDir.mkdirs()) {
                logger.info("Directory dei dati utente creata: " + email);
            } else {
                logger.severe("Errore nella creazione della cartella." + email);
            }
        }
        createUserDataFiles(userDir, email);
    }


    //Cartella dell'utente
    private static File getUserFolder(String email) {
        String mail = email.replaceAll("[^a-zA-Z0-9._@-]", "_");
        return new File(USER_DATA_DIR + mail);
    }


    private void createUserDataFiles(File userDir, String email) {
        String[] fileNames = {"characterSheets.csv", "joinedLobbies.csv", "favouriteLobbies.csv"};
        for (String fileName : fileNames) {
            File file = new File(userDir, fileName);
            if (!file.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(email);
                    writer.newLine();
                } catch (IOException e) {
                    logger.severe("Errore nella creazione della file: " + file.getPath() + e.getMessage());
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
                    return new User(data[0], data[1], new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file: " + e.getMessage());
        }
        return null;
    }


    public void saveUsersEntityData(User user) {
        File userDir = getUserFolder(user.getEmail());
        File characterFile = new File(userDir, "characters.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(characterFile))) {
            writer.write(user.getEmail());
            writer.newLine();
            for (CharacterSheet cs : user.getCharacterSheets()) {
                writer.write(serializeCharacterSheet(cs));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!userDir.exists()) {
            if (userDir.mkdirs()) {
                logger.info("Directory dei dati utente creata: " + user.getEmail());
            } else {
                logger.severe("Errore nella creazione della cartella." + user.getEmail());
            }
        }


        // Salvataggio delle joined lobbies
        File joinedFile = new File(userDir, "joinedLobbies.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(joinedFile))) {
            writer.write(user.getEmail());
            writer.newLine();
            for (Lobby lobby : user.getJoinedLobbies()) {
                writer.write(serializeLobby(lobby));
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore durante il salvataggio delle joined lobbies: " + e.getMessage());
        }
        // Salvataggio delle favourite lobbies
        File favFile = new File(userDir, "favouriteLobbies.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(favFile))) {
            writer.write(user.getEmail());
            writer.newLine();
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
        // Split usando il delimitatore ";"
        String[] parts = line.split(";", -1);
        String lobbyName = parts[0];
        String duration = parts[1];
        String type = parts[2];
        boolean owned = Boolean.parseBoolean(parts[3]);
        int numberOfPlayers = Integer.parseInt(parts[4]);
        List<String> players = new ArrayList<>();
        if (!parts[5].isEmpty()) {
            players = new ArrayList<>(Arrays.asList(parts[5].split("\\|")));
        }
        Lobby lobby = new Lobby(lobbyName, duration, type, owned, numberOfPlayers);
        // Aggiungiamo i players (si suppone che la lista ritornata da getPlayers() sia modificabile)
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
        // Impostiamo una password dummy (puoi adattare questa parte secondo le tue necessità)
        String dummyPassword = "******";
        return new User(user.getEmail(), dummyPassword, characterSheets, favouriteLobbies, joinedLobbies);
    }
}


/*
    @Override
    public User getUserByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    return new User(data[0], data[1], new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file: " + e.getMessage());
        }
        return null;
    }

    public void saveUserLobbies(String email, List<String> newLobbies) {
        List<String> allUserData = new ArrayList<>();
        boolean userFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(LOBBY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    Set<String> lobbiesSet = new HashSet<>();
                    for (int i = 1; i < data.length; i++) {
                        lobbiesSet.add(data[i]);
                    }
                    lobbiesSet.addAll(newLobbies);
                    allUserData.add(email + "," + String.join(",", lobbiesSet));
                    userFound = true;
                } else {
                    allUserData.add(line);
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file: " + e.getMessage());
        }

        if (!userFound) {
            allUserData.add(email + "," + String.join(",", newLobbies));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, false))) {
            for (String userData : allUserData) {
                writer.write(userData);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore nella scrittura delle lobby: " + e.getMessage());
        }
    }

    public List<String> getUserLobbies(String email) {
        List<String> lobbies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOBBY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    for (int i = 1; i < data.length; i++) {
                        lobbies.add(data[i]);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura delle lobby: " + e.getMessage());
        }
        return lobbies;
    }


    public void removeUserLobby(String email, String lobbyName) {
        List<String> allUserData = new ArrayList<>();
        boolean userFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(LOBBY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    userFound = true;
                    List<String> updatedLobbies = new ArrayList<>();

                    // Mantiene solo le lobby diverse da quella da rimuovere
                    for (int i = 1; i < data.length; i++) {
                        if (!data[i].equals(lobbyName)) {
                            updatedLobbies.add(data[i]);
                        }
                    }

                    // Se l'utente ha ancora lobby, lo aggiungiamo alla lista
                    if (!updatedLobbies.isEmpty()) {
                        allUserData.add(email + "," + String.join(",", updatedLobbies));
                    }
                } else {
                    allUserData.add(line);
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file delle lobby: " + e.getMessage());
            return;
        }

        // Scriviamo i dati aggiornati nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, false))) {
            for (String userData : allUserData) {
                writer.write(userData);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore nella scrittura delle lobby: " + e.getMessage());
        }

        if (userFound) {
            logger.info("Lobby '" + lobbyName + "' rimossa con successo per l'utente: " + email);
        } else {
            logger.warning("L'utente " + email + " non aveva la lobby: " + lobbyName);
        }
    }*/

