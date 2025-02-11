package it.uniroma2.marchidori.maininterface.utils;

import it.uniroma2.marchidori.maininterface.entity.CharacterInfo;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.CharacterStats;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileSystemHelperCSV {

    // Cartella base dove vengono creati i dati utente
    private static final String BASE_PATH = "src/main/java/it/uniroma2/marchidori/maininterface/";

    /**
     * Ritorna la cartella corrispondente all'utente (con email "sanificata").
     */
    private static File getUserFolder(String email) {
        String safeEmail = email.replaceAll("[^a-zA-Z0-9._@-]", "_");
        return new File(BASE_PATH, safeEmail);
    }

    /**
     * Salva su disco i dati dell'utente in tre file CSV:
     * - characterSheets.csv
     * - joinedLobbies.csv
     * - favouriteLobbies.csv
     */
    public static void saveUserData(User user) throws IOException {
        // Crea la cartella base se non esiste
        File baseDir = new File(BASE_PATH);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        // Crea/ottieni la cartella dell'utente
        File userDir = getUserFolder(user.getEmail());
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        // Salvataggio dei fogli personaggio
        File characterFile = new File(userDir, "characterSheets.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(characterFile))) {
            for (CharacterSheet cs : user.getCharacterSheets()) {
                writer.write(serializeCharacterSheet(cs));
                writer.newLine();
            }
        }

        // Salvataggio delle joined lobbies
        File joinedFile = new File(userDir, "joinedLobbies.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(joinedFile))) {
            for (Lobby lobby : user.getJoinedLobbies()) {
                writer.write(serializeLobby(lobby));
                writer.newLine();
            }
        }

        // Salvataggio delle favourite lobbies
        File favFile = new File(userDir, "favouriteLobbies.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(favFile))) {
            for (Lobby lobby : user.getFavouriteLobbies()) {
                writer.write(serializeLobby(lobby));
                writer.newLine();
            }
        }
    }

    /**
     * Carica i dati dell'utente (tramite i file CSV) e costruisce un oggetto User.
     * (In questo esempio, password e ruolo non vengono gestiti e vengono impostati a valori dummy.)
     */
    public static User loadUserData(String email) throws IOException {
        File userDir = getUserFolder(email);
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
            }
        }

        // Impostiamo una password dummy (puoi adattare questa parte secondo le tue necessità)
        String dummyPassword = "******";
        return new User(email, dummyPassword, characterSheets, favouriteLobbies, joinedLobbies);
    }

    // =============================================
    // METODI DI SERIALIZZAZIONE/DESERIALIZZAZIONE CSV
    // =============================================

    /**
     * Serializza una lobby in una riga CSV.
     * Formato: lobbyName;duration;type;owned;numberOfPlayers;players
     * (La lista dei players viene unita usando il delimitatore "|" )
     */
    public static String serializeLobby(Lobby lobby) {
        String playersJoined = String.join("|", lobby.getPlayers());
        return lobby.getLobbyName() + ";" +
                lobby.getDuration() + ";" +
                lobby.getType() + ";" +
                lobby.isOwned() + ";" +
                lobby.getNumberOfPlayers() + ";" +
                playersJoined;
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
     * Serializza un CharacterSheet in una riga CSV.
     * Formato:
     * name;race;age;classe;level;strength;dexterity;intelligence;wisdom;charisma;constitution
     */
    public static String serializeCharacterSheet(CharacterSheet cs) {
        CharacterInfo info = cs.getCharacterInfo();
        CharacterStats stats = cs.getCharacterStats();
        return info.getName() + ";" +
                info.getRace() + ";" +
                info.getAge() + ";" +
                info.getClasse() + ";" +
                info.getLevel() + ";" +
                stats.getStrength() + ";" +
                stats.getDexterity() + ";" +
                stats.getIntelligence() + ";" +
                stats.getWisdom() + ";" +
                stats.getCharisma() + ";" +
                stats.getConstitution();
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
}
