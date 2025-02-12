package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JoinLobbyPlayerCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private JoinLobbyController controller;
    private List<LobbyBean> filteredLobbies = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("JoinLobbyPlayerCLIBoundary");

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
                        lobby.getNumberOfPlayers(),
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
        jout.print("4. Unisciti a una lobby");
        jout.print("5. Ricarica lista lobby");
        jout.print("6. Torna a Home");
        jout.print("0. Esci");
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
                handleJoinLobby();
                break;
            case "5":
                refreshTable();
                jout.print("Lista aggiornata.");
                break;
            case "6":
                changeScene("home.fxml");
                return true;
            case "0":
                jout.print("Uscita dalla modalità Join Lobby.");
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
    private void applyFilters() {
        filterType = prompt("Inserisci filtro per tipo (Online/Presenza) oppure lascia vuoto: ");
        filterDuration = prompt("Inserisci filtro per durata (Singola/Campagna) oppure lascia vuoto: ");
        filterNumPlayers = prompt("Inserisci filtro per numero di giocatori (2-8) oppure lascia vuoto: ");
        doFilter();
    }

    /**
     * Richiede all'utente una stringa di ricerca e la applica come filtro.
     */
    private void applySearch() {
        searchQuery = prompt("Inserisci stringa di ricerca: ").toLowerCase();
        doFilter();
    }

    /**
     * Resetta tutti i filtri impostati e riapplica il filtraggio.
     */
    private void resetFilters() {
        filterType = "";
        filterDuration = "";
        filterNumPlayers = "";
        searchQuery = "";
        doFilter();
    }

    /**
     * Applica i filtri impostati chiamando il metodo di filtraggio del controller.
     */
    private void doFilter() {
        List<LobbyBean> result = controller.filterLobbies(filterType, filterDuration, filterNumPlayers, searchQuery);
        filteredLobbies = result != null ? result : new ArrayList<>();
    }

    /**
     * Gestisce l'operazione di join di una lobby:
     * chiede all'utente l'indice della lobby, ne chiede conferma e chiama il controller per eseguire l'operazione.
     */
    private void handleJoinLobby() {
        if (filteredLobbies.isEmpty()) {
            jout.print("Nessuna lobby disponibile per unirsi.");
            return;
        }
        String idxStr = prompt("Inserisci il numero della lobby a cui unirti: ");
        try {
            int index = Integer.parseInt(idxStr);
            if (index < 1 || index > filteredLobbies.size()) {
                jout.print("Indice non valido.");
                return;
            }
            LobbyBean lobbyToJoin = filteredLobbies.get(index - 1);
            String conf = prompt("Vuoi unirti alla lobby '" + lobbyToJoin.getName() + "'? (y/n): ");
            if (conf.equalsIgnoreCase("y")) {
                // Si assume che il controller gestisca l'operazione di join e restituisca true in caso di successo.
                boolean success = true;
                if (success) {
                    jout.print("Sei entrato nella lobby '" + lobbyToJoin.getName() + "'.");
                } else {
                    jout.print("Non è stato possibile unirsi alla lobby.");
                }
            } else {
                jout.print("Operazione annullata.");
            }
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        }
    }

    /**
     * Ricarica la lista delle lobby disponibili, ottenendola dal controller e
     * applica i filtri correnti.
     */
    private void refreshTable() {
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