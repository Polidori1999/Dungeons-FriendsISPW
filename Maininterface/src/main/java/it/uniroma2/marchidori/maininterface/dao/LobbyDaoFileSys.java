package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

        // Apre il file in modalità append per aggiungere la nuova lobby
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, true))) {
            String lobbyLine = convertLobbyToString(lobby);
            writer.write(lobbyLine);
            writer.newLine();
        }
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
    public List<Lobby> getLobbiesFromSys() throws IOException {
        File file = new File(LOBBY_FILE_PATH);
        if (!file.exists()) {
            // Se il file non esiste, restituisce una lista vuota
            return new ArrayList<>();
        }

        // Legge tutte le righe dal file
        List<String> lines = Files.readAllLines(Paths.get(LOBBY_FILE_PATH));
        return lines.stream().map(line -> {
            String[] tokens = line.split(";");
            // Il formato atteso è: lobbyName;duration;type;owned;numberOfPlayers;infoLink
            if (tokens.length == 6) {
                String lobbyName = tokens[0];
                String duration = tokens[1];
                String type = tokens[2];
                boolean owned = Boolean.parseBoolean(tokens[3]);
                int numberOfPlayers = Integer.parseInt(tokens[4]);
                String infoLink = tokens[5];
                return new Lobby(lobbyName, duration, type, owned, numberOfPlayers, infoLink);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Converte una Lobby in una stringa formattata per la memorizzazione su file.
     * Utilizza il carattere ';' come delimitatore.
     *
     * @param lobby la lobby da convertire
     * @return la rappresentazione in stringa della lobby
     */
    private String convertLobbyToString(Lobby lobby) {
        return lobby.getLobbyName() + ";"
                + lobby.getDuration() + ";"
                + lobby.getType() + ";"
                + lobby.isOwned() + ";"
                + lobby.getNumberOfPlayers() + ";"
                + lobby.getInfoLink();
    }
}
