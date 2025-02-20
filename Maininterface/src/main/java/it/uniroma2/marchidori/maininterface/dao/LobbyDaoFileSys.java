package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.utils.Alert;

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

    //metodo che aggiunge una linea ovvero una lobby nel file
    public boolean addLobby(Lobby lobby) {
        // Crea la directory di base se non esiste
        File dir = new File(BASE_DIR);
        if (!dir.exists()) {
            boolean dirCreated = dir.mkdirs();
            if (!dirCreated) {
                logger.severe("Impossibile creare la directory di base: " + BASE_DIR);
                return false;  // Restituisce false se la directory non può essere creata
            }
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

        // Scrive in append nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, true))) {
            writer.write(linea);
            writer.newLine();
            return true;  // Restituisce true se l'operazione è riuscita
        } catch (IOException e) {
            logger.severe("Errore durante la scrittura sul file: " + e.getMessage());
            return false;
        }
    }


    //metodo che trova e aggiorna una lobby nel file
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

    //metodo che trova ed elimina una linea nel file
    @Override
    public boolean deleteLobby(String lobbyName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(LOBBY_FILE_PATH));
            List<String> updatedLines = new ArrayList<>(lines.stream()
                    .filter(line -> {
                        String[] tokens = line.split(";", -1);
                        return tokens.length > 0 && !tokens[0].equals(lobbyName);
                    })
                    .toList());
            Files.write(Paths.get(LOBBY_FILE_PATH), updatedLines);
            return true; // Operazione riuscita
        } catch (IOException e) {

            logger.severe("Errore nel tentativo di eliminare la lobby: " + lobbyName);

            Alert.showError("Errore nel file", "Si è verificato un errore durante l'eliminazione della lobby.");

            return false; // Indica che l'operazione non è riuscita
        }
    }



    //Legge il file e restituisce la lista di tutte le lobby
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

    //legge una singola linea del file ovvero una singola lobby
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


    //converte una Lobby in una stringa formattata per la memorizzazione.
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