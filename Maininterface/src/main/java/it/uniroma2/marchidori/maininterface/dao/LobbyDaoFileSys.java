package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LobbyDaoFileSys implements LobbyDAO {

    private static final String BASE_DIR = "src/main/java/it/uniroma2/marchidori/maininterface/repository/";
    private static final String LOBBY_FILE_PATH = BASE_DIR + "lobby.txt";
    private static final Logger logger = Logger.getLogger(LobbyDaoFileSys.class.getName());

    /**
     * Aggiunge (appende) una lobby al file, utilizzando un formato con 7 campi:
     * [0] lobbyName, [1] duration, [2] liveOnline, [3] maxOfPlayers,
     * [4] owner, [5] infoLink, [6] joinedPlayersCount.
     */
    public void addLobby(Lobby lobby) throws IOException {
        // Crea la directory di base se non esiste
        File dir = new File(BASE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Prepara i 7 campi fissi
        String[] fields = new String[7];
        fields[0] = lobby.getLobbyName();
        fields[1] = lobby.getDuration();
        fields[2] = lobby.getLiveOnline();
        fields[3] = String.valueOf(lobby.getMaxOfPlayers());
        fields[4] = lobby.getOwner();
        fields[5] = lobby.getInfoLink();
        fields[6] = String.valueOf(lobby.getJoinedPlayersCount());

        String linea = String.join(";", fields);

        // Scrive la riga in fondo al file (append)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, true))) {
            writer.write(linea);
            writer.newLine();
        } catch (IOException e) {
            logger.severe("Errore durante la scrittura sul file: " + e.getMessage());
        }
    }

    /**
     * Aggiorna una lobby già esistente nel file, sostituendo la sua riga con quella aggiornata.
     */
    public void updateLobby(Lobby updatedLobby) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(LOBBY_FILE_PATH));
        boolean found = false;
        for (int i = 0; i < lines.size() && !found; i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                String[] tokens = line.split(";", -1);
                if (tokens.length > 0 && tokens[0].equals(updatedLobby.getLobbyName())) {
                    String newLine = convertLobbyToString(updatedLobby);
                    lines.set(i, newLine);
                    found = true;
                }
            }
        }
        if (!found) {
            logger.severe("Lobby con nome '" + updatedLobby.getLobbyName() + "' non trovata.");
        }
        Files.write(Paths.get(LOBBY_FILE_PATH), lines);
    }

    /**
     * Elimina dal file la lobby avente il nome specificato.
     */
    @Override
    public void deleteLobby(String lobbyName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(LOBBY_FILE_PATH));
        List<String> updatedLines = new ArrayList<>(lines.stream()
                .filter(line -> {
                    String[] tokens = line.split(";", -1);
                    return tokens.length > 0 && !tokens[0].equals(lobbyName);
                })
                .toList());
        Files.write(Paths.get(LOBBY_FILE_PATH), updatedLines);
    }

    /**
     * Legge il file e restituisce la lista di tutte le lobby (deserializzate).
     */
    public List<Lobby> getLobby() {
        List<Lobby> lobbyList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOBBY_FILE_PATH))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Lobby lobby = parseLobbyLine(linea);
                if (lobby != null) {
                    lobbyList.add(lobby);
                }
            }
        } catch (IOException e) {
            logger.severe("Errore durante la lettura dal file: " + e.getMessage());
        }
        return lobbyList;
    }

    /**
     * Parses a single line from the lobby file into a Lobby object.
     * Returns null if the line is empty or invalid.
     */
    private Lobby parseLobbyLine(String linea) {
        if (linea.trim().isEmpty()) {
            return null;
        }
        String[] campi = linea.split(";", -1);
        if (campi.length < 7) {
            logger.log(Level.SEVERE, "Linea non valida (meno di 7 campi): {0}", linea);
            return null;
        }

        String name = campi[0];
        String duration = campi[1];
        String liveOnline = campi[2];

        int maxOfPlayers;
        try {
            maxOfPlayers = Integer.parseInt(campi[3]);
        } catch (NumberFormatException e) {
            logger.severe("Numero di giocatori non valido in: " + linea);
            return null;
        }

        String owner = campi[4];
        String infoLink = campi[5];

        int joinedPlayersCount;
        try {
            joinedPlayersCount = Integer.parseInt(campi[6]);
        } catch (NumberFormatException e) {
            logger.severe("Contatore dei giocatori non valido in: " + linea);
            return null;
        }

        return new Lobby(name, duration, liveOnline, maxOfPlayers, owner, infoLink, joinedPlayersCount);
    }


    /**
     * Converte una Lobby in una stringa formattata (con 7 campi) per la memorizzazione.
     */
    public static String convertLobbyToString(Lobby lobby) {
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
}
