package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LobbyDaoFileSys {

    private static final String BASE_DIR = "src/main/java/it/uniroma2/marchidori/maininterface/repository/";
    private static final String LOBBY_FILE_PATH = BASE_DIR + "lobby.txt";

    /**
     * Aggiunge la lobby in input alla lista (file) esistente.
     *
     * @param lobby la lobby da aggiungere
     * @throws IOException se si verifica un errore nella scrittura del file
     */
    public void addLobby(Lobby lobby) throws IOException {
        // Verifica ed eventualmente crea la directory di base
        File dir = new File(BASE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, true))) {
            // Converte la lista dei joinedPlayers in una stringa separata da ";"
            String joinedPlayersString = String.join(",", lobby.getJoinedPlayers());

            // Costruisce la linea con i campi separati da virgola
            String linea = String.join(";",
                    lobby.getLobbyName(),
                    lobby.getDuration(),
                    lobby.getLiveOnline(),
                    String.valueOf(lobby.getMaxOfPlayers()),
                    lobby.getOwner(),
                    lobby.getInfoLink(),
                    joinedPlayersString
            );
            writer.write(linea);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura sul file: " + e.getMessage());
        }
    }

    public void updateLobby(Lobby updatedLobby) throws IOException {
        // Legge tutte le righe presenti nel file
        List<String> lines = Files.readAllLines(Paths.get(LOBBY_FILE_PATH));

        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            // Splitta la riga usando ";" come delimitatore, in quanto è lo stesso usato in addLobby
            String[] tokens = line.split(";", -1);
            // Controlla che la prima colonna (nome della lobby) corrisponda
            if (tokens.length > 0 && tokens[0].equals(updatedLobby.getLobbyName())) {
                // Sostituisce la riga con la rappresentazione aggiornata della lobby
                lines.set(i, convertLobbyToString(updatedLobby));
                found = true;
                break;
            }
        }

        if (!found) {
            System.err.println("Lobby con nome '" + updatedLobby.getLobbyName() + "' non trovata.");
        }
        // Riscrive il file con le righe aggiornate
        Files.write(Paths.get(LOBBY_FILE_PATH), lines);
    }


    /**
     * Elimina dal file la lobby con il nome specificato.
     *
     * @param lobbyName il nome della lobby da cancellare
     * @throws IOException se si verifica un errore nella lettura o scrittura del file
     */
    public void deleteLobby(String lobbyName) throws IOException {
        // Legge tutte le righe presenti nel file
        List<String> lines = Files.readAllLines(Paths.get(LOBBY_FILE_PATH));

        // Filtra le righe escludendo quella che corrisponde al nome della lobby da eliminare
        List<String> updatedLines = lines.stream()
                .filter(line -> {
                    // Supponiamo che il formato sia: lobbyName;duration;type;owned;numberOfPlayers;infoLink
                    String[] tokens = line.split(";");
                    return tokens.length > 0 && !tokens[0].equals(lobbyName);
                })
                .collect(Collectors.toList());

        // Riscrive il file con le righe aggiornate
        Files.write(Paths.get(LOBBY_FILE_PATH), updatedLines);
    }

    /**
     * Restituisce una lista di tutte le lobby presenti nel file.
     *
     * @return lista di oggetti Lobby
     * @throws IOException se si verifica un errore nella lettura del file
     */
    public static List<Lobby> getLobbiesFromSys() {
        List<Lobby> lobbyList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOBBY_FILE_PATH))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println("Riga letta: " + linea); // Debug: stampa ogni riga
                if (linea.trim().isEmpty()) {
                    continue;
                }
                // Usa il delimitatore ";" per separare i campi (coerente con addLobby)
                String[] campi = linea.split(";", -1);
                if (campi.length < 7) {
                    System.err.println("Linea non valida (numero di campi insufficiente): " + linea);
                    continue;
                }

                String name = campi[0];
                String duration = campi[1];
                String liveOnline = campi[2];
                int maxOfPlayers;
                try {
                    maxOfPlayers = Integer.parseInt(campi[3]);
                } catch (NumberFormatException e) {
                    System.err.println("Numero di giocatori non valido in: " + linea);
                    continue;
                }
                String owner = campi[4];
                String infoLink = campi[5];

                // Per i joinedPlayers, splitta usando la virgola (coerente con addLobby)
                List<String> joinedPlayers = new ArrayList<>();
                if (!campi[6].isEmpty()) {
                    joinedPlayers = Arrays.asList(campi[6].split(","));
                }

                Lobby lobby = new Lobby(name, duration, liveOnline, maxOfPlayers, owner, infoLink, joinedPlayers);
                lobbyList.add(lobby);
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura dal file: " + e.getMessage());
        }
        System.out.println("Numero di lobby lette: " + lobbyList.size()); // Debug: conta oggetti Lobby
        return lobbyList;
    }






    /**
     * Converte una Lobby in una stringa formattata per la memorizzazione su file.
     * Utilizza il carattere ';' come delimitatore.
     *
     * @param lobby la lobby da convertire
     * @return la rappresentazione in stringa della lobby
     */
    public static String convertLobbyToString(Lobby lobby) {
        if (lobby == null) {
            return "Lobby is null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(lobby.getLobbyName()).append(";")
                .append(lobby.getDuration()).append(";")
                .append(lobby.getLiveOnline()).append(";")
                .append(lobby.getMaxOfPlayers()).append(";")
                .append(lobby.getOwner()).append(";")
                .append(lobby.getInfoLink()).append(";");

        List<String> players = lobby.getJoinedPlayers();
        if (players != null && !players.isEmpty()) {
            sb.append(String.join(", ", players));
        } else {
            sb.append("");
        }

        return sb.toString();
    }


}
