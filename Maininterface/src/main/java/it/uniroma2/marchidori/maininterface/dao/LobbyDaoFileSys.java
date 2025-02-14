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


    public void addLobby(Lobby lobby) throws IOException {
        // Crea la directory di base se non esiste
        File dir = new File(BASE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Calcola il numero totale di campi (6 campi base + maxOfPlayers per i giocatori)
        int totalFields = 6 + lobby.getMaxOfPlayers();
        String[] fields = new String[totalFields];

        // Riempie i primi 6 campi
        fields[0] = lobby.getLobbyName();                // nome
        fields[1] = lobby.getDuration();                 // durata
        fields[2] = lobby.getLiveOnline();               // liveOnline
        fields[3] = String.valueOf(lobby.getMaxOfPlayers()); // maxPlayers
        fields[4] = lobby.getOwner();                    // owner
        fields[5] = lobby.getInfoLink();                 // infoLink

        // Riempie i campi dedicati ai giocatori
        // Se la lista dei giocatori ha meno di maxOfPlayers, i rimanenti campi saranno stringhe vuote.
        List<String> joinedPlayers = lobby.getJoinedPlayers();
        for (int i = 0; i < lobby.getMaxOfPlayers(); i++) {
            if (joinedPlayers != null && i < joinedPlayers.size()) {
                fields[6 + i] = joinedPlayers.get(i);
            } else {
                fields[6 + i] = "";  // slot vuoto
            }
        }

        // Converte l'array in una singola riga separata da ';'
        String linea = String.join(";", fields);

        // Scrive la riga in fondo al file (append = true)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, true))) {
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
            // Splitta la riga usando ";" (lo stesso usato in addLobby e getLobbiesFromSys)
            String[] tokens = line.split(";", -1);

            // Controlla che la prima colonna (nome della lobby) corrisponda
            if (tokens.length > 0 && tokens[0].equals(updatedLobby.getLobbyName())) {
                // Ricostruisci la riga aggiornata
                String newLine = convertLobbyToString(updatedLobby);
                // Sostituisce la vecchia riga con la nuova
                lines.set(i, newLine);
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
                // Salta righe vuote
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] campi = linea.split(";", -1);

                // Controllo minimo: servono almeno 6 campi per poter leggere i dati base.
                if (campi.length < 6) {
                    System.err.println("Linea non valida (meno di 6 campi): " + linea);
                    continue;
                }

                // Legge i dati base
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

                // Ricostruisce la lista dei giocatori
                List<String> joinedPlayers = new ArrayList<>();
                // Indice dal quale iniziano i giocatori
                int playersStartIndex = 6;
                // Indice finale: 6 + maxOfPlayers - 1
                int playersEndIndex = playersStartIndex + maxOfPlayers - 1;

                // Se la riga ha meno campi di quelli necessari, i rimanenti li consideriamo vuoti
                for (int i = playersStartIndex; i <= playersEndIndex; i++) {
                    if (i < campi.length) {
                        joinedPlayers.add(campi[i]);
                    } else {
                        joinedPlayers.add("");
                    }
                }

                // Crea la Lobby
                Lobby lobby = new Lobby(name, duration, liveOnline, maxOfPlayers, owner, infoLink, joinedPlayers);
                lobbyList.add(lobby);
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura dal file: " + e.getMessage());
        }
        return lobbyList;
    }







    /**
     * Converte una Lobby in una stringa formattata per la memorizzazione su file.
     * Utilizza il carattere ';' come delimitatore.
     *
     * @param lobby la lobby da convertire
     * @return la rappresentazione in stringa della lobby
     */
    /**
     * Converte una Lobby in una stringa formattata per la memorizzazione su file.
     * Formato: 6 campi base + `maxOfPlayers` campi per i giocatori, tutti separati da ";".
     */
    public static String convertLobbyToString(Lobby lobby) {
        if (lobby == null) {
            return "Lobby is null";
        }

        // Numero totale di campi = 6 campi base + maxOfPlayers
        int totalFields = 6 + lobby.getMaxOfPlayers();
        String[] fields = new String[totalFields];

        // Campi base
        fields[0] = lobby.getLobbyName();                       // nome
        fields[1] = lobby.getDuration();                        // durata
        fields[2] = lobby.getLiveOnline();                      // liveOnline
        fields[3] = String.valueOf(lobby.getMaxOfPlayers());    // maxPlayers
        fields[4] = lobby.getOwner();                           // owner
        fields[5] = lobby.getInfoLink();                        // infoLink

        // Campi dedicati ai giocatori
        List<String> joinedPlayers = lobby.getJoinedPlayers();
        for (int i = 0; i < lobby.getMaxOfPlayers(); i++) {
            if (joinedPlayers != null && i < joinedPlayers.size()) {
                fields[6 + i] = joinedPlayers.get(i);
            } else {
                fields[6 + i] = "";  // slot vuoto
            }
        }

        // Unisci tutti i campi con ";"
        return String.join(";", fields);
    }



}
