/*package it.uniroma2.marchidori.maininterface.utils;

import it.uniroma2.marchidori.maininterface.entity.CharacterInfo;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.CharacterStats;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.utils.FileSystemHelperCSV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        // --- Creazione dei dati di esempio ---

        // Creazione dei fogli personaggio (CharacterSheet)
        CharacterInfo info1 = new CharacterInfo("Frodo", "Hobbit", 33, "Portatore dell'Anello", 1);
        CharacterStats stats1 = new CharacterStats(8, 12, 14, 13, 10, 9);
        CharacterSheet cs1 = new CharacterSheet(info1, stats1);

        CharacterInfo info2 = new CharacterInfo("Samwise", "Hobbit", 35, "Giardiniere", 1);
        CharacterStats stats2 = new CharacterStats(10, 10, 12, 11, 13, 10);
        CharacterSheet cs2 = new CharacterSheet(info2, stats2);

        List<CharacterSheet> characterSheets = new ArrayList<>();
        characterSheets.add(cs1);
        characterSheets.add(cs2);

        // Creazione delle lobby
        Lobby lobby1 = new Lobby("Shire Adventure", "Campagna", "Online", true, 5);
        lobby1.getPlayers().add("Frodo");
        lobby1.getPlayers().add("Samwise");

        Lobby lobby2 = new Lobby("Mordor Quest", "Singola", "Presenza", false, 4);
        lobby2.getPlayers().add("Frodo");

        List<Lobby> joinedLobbies = new ArrayList<>();
        joinedLobbies.add(lobby1);

        List<Lobby> favouriteLobbies = new ArrayList<>();
        favouriteLobbies.add(lobby2);

        // Creazione dell'utente con email e password (password verrà impostata a valore dummy in fase di caricamento)
        User user = new User("frodo@shire.com", "myPrecious", characterSheets, favouriteLobbies, joinedLobbies);

        // --- Salvataggio e caricamento dei dati ---
        try {
            // Salva i dati dell'utente in CSV nella cartella "usersData/frodo@shire.com" (nome sanificato)
            FileSystemHelperCSV.saveUserData(user);
            System.out.println("Dati utente salvati con successo.");

            // Carica i dati dell'utente
            User loadedUser = FileSystemHelperCSV.loadUserData("frodo@shire.com");
            if (loadedUser != null) {
                System.out.println("Dati utente caricati con successo!");
                System.out.println("Email: " + loadedUser.getEmail());

                System.out.println("Character Sheets:");
                for (CharacterSheet cs : loadedUser.getCharacterSheets()) {
                    System.out.println("  " + cs); // Assicurati di aver implementato un toString() in CharacterSheet per visualizzare i dati
                }

                System.out.println("Joined Lobbies:");
                for (Lobby lobby : loadedUser.getJoinedLobbies()) {
                    System.out.println("  " + lobby.getLobbyName() + " - Players: " + lobby.getPlayers());
                }

                System.out.println("Favourite Lobbies:");
                for (Lobby lobby : loadedUser.getFavouriteLobbies()) {
                    System.out.println("  " + lobby.getLobbyName() + " - Players: " + lobby.getPlayers());
                }
            } else {
                System.out.println("Utente non trovato!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
