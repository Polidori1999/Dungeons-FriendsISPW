package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class JoinLobbyDMCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private JoinLobbyController controller;
    private ObservableList<LobbyBean> filteredLobbies;
    private final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("JoinLobbyDMCLIBoundary");

    // Parametri per il filtraggio
    private String filterType = "";
    private String filterDuration = "";
    private String filterNumPlayers = "";
    private String searchQuery = "";

    @Override
    public void run() throws IOException {
        if (currentUser == null) {
            jout.print("ERRORE: Utente non inizializzato.");
            return;
        }
        refreshTable();
        boolean exit = false;
        while (!exit) {
            displayLobbyList();
            displayMenu();
            String input = prompt("Scegli un'opzione: ");
            exit = processInput(input);
            jout.print(""); // Riga vuota per separare le iterazioni
        }
    }

    /**
     * Visualizza la lista (tabellare) delle lobby filtrate.
     */
    private void displayLobbyList() {
        jout.print("=== Lista Lobby Disponibili ===");
        if (filteredLobbies.isEmpty()) {
            jout.print("Nessuna lobby trovata con i filtri attuali.");
        } else {
            jout.print(String.format("%-3s %-20s %-15s %-10s %-10s", "No", "Nome", "Giocatori", "Durata", "Online"));
            int i = 1;
            for (LobbyBean lobby : filteredLobbies) {
                jout.print(String.format("%-3d %-20s %-15s %-10s %-10s",
                        i,
                        lobby.getName(),
                        lobby.getMaxOfPlayers(),
                        lobby.getDuration(),
                        lobby.getLiveOnline()));
                i++;
            }
        }
    }

    /**
     * Visualizza il menu delle operazioni disponibili in modalità CLI.
     */
    private void displayMenu() {
        jout.print("=== Menu Join Lobby ===");
        jout.print("1. Applica filtri");
        jout.print("2. Reset filtri");
        jout.print("3. Cerca per nome");
        jout.print("4. Ricarica lista lobby");
        jout.print("0. Torna a Home");
    }

    /**
     * Richiede in console un input all'utente.
     */
    private String prompt(String message) {
        jout.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * Elabora l'input utente e delega l'esecuzione delle operazioni.
     *
     * @param input La scelta dell'utente.
     * @return true se si vuole uscire dalla modalità CLI, false altrimenti.
     * @throws IOException in caso di errori nel cambio scena.
     */
    private boolean processInput(String input) throws IOException {
        switch (input) {
            case "1":
                applyFilters();
                break;
            case "2":
                resetFilters();
                jout.print("Filtri resettati.");
                break;
            case "3":
                applySearch();
                break;
            case "4":
                refreshTable();
                jout.print("Lista aggiornata.");
                break;
            case "0":
                changeScene(SceneNames.HOME);
                return true;
            default:
                jout.print("Opzione non valida, riprova.");
        }
        return false;
    }

    /**
     * Richiede all'utente di impostare i filtri per tipo, durata e numero di giocatori,
     * e applica il filtraggio.
     */
    private void applyFilters() throws IOException {
        // Filtro per tipo (Online/Presenza)
        jout.print("Scegli filtro per tipo:");
        jout.print("1. Online");
        jout.print("2. Presenza");
        jout.print("0. Lascia vuoto");
        String choice = prompt("Inserisci il numero dell'opzione: ");
        switch (choice) {
            case "1":
                filterType = "Online";
                break;
            case "2":
                filterType = "Presenza";
                break;
            default:
                filterType = "";
                break;
        }

        // Filtro per durata (Singola/Campagna)
        jout.print("Scegli filtro per durata:");
        jout.print("1. Singola");
        jout.print("2. Campagna");
        jout.print("0. Lascia vuoto");
        choice = prompt("Inserisci il numero dell'opzione: ");
        switch (choice) {
            case "1":
                filterDuration = "Singola";
                break;
            case "2":
                filterDuration = "Campagna";
                break;
            default:
                filterDuration = "";
                break;
        }

        // Filtro per numero di giocatori (2, 4, 6, 8)
        jout.print("Scegli filtro per numero di giocatori:");
        jout.print("1. 2 giocatori");
        jout.print("2. 4 giocatori");
        jout.print("3. 6 giocatori");
        jout.print("4. 8 giocatori");
        jout.print("0. Lascia vuoto");
        choice = prompt("Inserisci il numero dell'opzione: ");
        switch (choice) {
            case "1":
                filterNumPlayers = "2";
                break;
            case "2":
                filterNumPlayers = "4";
                break;
            case "3":
                filterNumPlayers = "6";
                break;
            case "4":
                filterNumPlayers = "8";
                break;
            default:
                filterNumPlayers = "";
                break;
        }

        doFilter();
    }

    /**
     * Richiede all'utente una stringa di ricerca e la applica come filtro.
     */
    private void applySearch() throws IOException {
        searchQuery = prompt("Inserisci stringa di ricerca: ").toLowerCase();
        doFilter();
    }

    /**
     * Resetta tutti i filtri impostati e riapplica il filtraggio.
     */
    private void resetFilters() throws IOException {
        filterType = "";
        filterDuration = "";
        filterNumPlayers = "";
        searchQuery = "";
        doFilter();
    }

    /**
     * Applica i filtri impostati chiamando il metodo di filtraggio del controller.
     */
    private void doFilter() throws IOException {
        List<LobbyBean> result = controller.filterLobbies(filterType, filterDuration, filterNumPlayers, searchQuery);
        filteredLobbies.setAll(result);
    }


    /**
     * Ricarica la lista delle lobby disponibili, ottenendola dal controller e
     * applica i filtri correnti.
     */
    private void refreshTable() throws IOException {
        if (controller != null) {
            doFilter();
        }
    }

    /**
     * Simula il cambio scena (in ambiente CLI si limita a stampare un messaggio).
     *
     * @param fxml Il nome della scena verso cui cambiare.
     * @throws IOException in caso di errori nel cambio scena.
     */
    private void changeScene(String fxml) throws IOException {
        jout.print("Cambio scena verso " + fxml + "...");
        SceneSwitcher.changeScene(null, fxml, currentUser);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (JoinLobbyController) logicController;
    }
}