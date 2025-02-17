package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class JoinLobbyDMCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    protected UserBean currentUser;
    protected JoinLobbyController controller;
    protected ObservableList<LobbyBean> filteredLobbies;
    private final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("JoinLobbyDMCLIBoundary");

    // Parametri per il filtraggio
    protected String filterType = "";
    protected String filterDuration = "";
    protected String filterNumPlayers = "";
    protected String searchQuery = "";

    @Override
    public void run() throws IOException {
        List<LobbyBean> initial = Converter.convertLobbyListEntityToBean(controller.getLobbies());
        filteredLobbies = FXCollections.observableArrayList(initial);
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
        changeScene(SceneNames.HOME);
    }

    /**
     * Visualizza la lista (tabellare) delle lobby filtrate.
     */
    protected void displayLobbyList() {
        jout.print("=== Lista Lobby Disponibili ===");

        if (filteredLobbies.isEmpty()) {
            jout.print("Nessuna lobby trovata con i filtri attuali.");
        } else {
            jout.print(String.format("%-3s %-20s %-15s %-10s %-10s", "No", "Nome", "Giocatori", "Durata", "Online"));

            int i = 1;
            for (LobbyBean lobby : filteredLobbies) {
                // Verifica se la lobby è già joinata dall'utente
                if (controller.isLobbyJoined(lobby)) {
                    continue;  // Se è joinata, salta questa iterazione e non la stampare
                }

                // Stampa le informazioni della lobby non ancora joinata
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
    protected String prompt(String message) {
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
    private boolean processInput(String input) {
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
    protected void applyFilters()  {
        // Filtro per tipo (Online/Presenza)
        jout.print("Scegli filtro per tipo:");
        jout.print("1. Online");
        jout.print("2. Live");
        jout.print("0. Lascia vuoto");
        String choice = prompt("Inserisci il numero dell'opzione: ");
        switch (choice) {
            case "1":
                filterType = "Online";
                break;
            case "2":
                filterType = "Live";
                break;
            default:
                filterType = "";
                break;
        }

        // Filtro per durata (Singola/Campagna)
        jout.print("Scegli filtro per durata:");
        jout.print("1. One-Shot");
        jout.print("2. Campaign");
        jout.print("0. Lascia vuoto");
        choice = prompt("Inserisci il numero dell'opzione: ");
        switch (choice) {
            case "1":
                filterDuration = "One-Shot";
                break;
            case "2":
                filterDuration = "Campaign";
                break;
            default:
                filterDuration = "";
                break;
        }
        doFilter();
    }

    /**
     * Richiede all'utente una stringa di ricerca e la applica come filtro.
     */
    protected void applySearch() {
        searchQuery = prompt("Inserisci stringa di ricerca: ").toLowerCase();
        doFilter();
    }

    /**
     * Resetta tutti i filtri impostati e riapplica il filtraggio.
     */
    protected void resetFilters() {
        filterType = "";
        filterDuration = "";
        filterNumPlayers = "";
        searchQuery = "";
        doFilter();
    }

    /**
     * Applica i filtri impostati chiamando il metodo di filtraggio del controller.
     */
    protected void doFilter() {
        List<LobbyBean> result = controller.filterLobbies(filterType, filterDuration, searchQuery);
        filteredLobbies.setAll(result);
    }


    /**
     * Ricarica la lista delle lobby disponibili, ottenendola dal controller e
     * applica i filtri correnti.
     */
    protected void refreshTable() {
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
    protected void changeScene(String fxml) throws IOException {
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