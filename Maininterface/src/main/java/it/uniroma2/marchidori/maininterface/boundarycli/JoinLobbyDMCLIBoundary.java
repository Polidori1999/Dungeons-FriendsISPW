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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JoinLobbyDMCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    protected UserBean currentUser;
    protected JoinLobbyController controller;
    protected ObservableList<LobbyBean> filteredLobbies;
    private final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("JoinLobbyDMCLIBoundary");

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


    protected void displayLobbyList() {
        jout.print("=== Lista Lobby Disponibili ===");

        if (filteredLobbies.isEmpty()) {
            jout.print("Nessuna lobby trovata con i filtri attuali.");
        } else {
            jout.print(String.format("%-3s %-20s %-15s %-10s %-10s", "No", "Nome", "Giocatori", "Durata", "Online"));

            int i = 1;
            for (LobbyBean lobby : filteredLobbies) {
                if (controller.isLobbyJoined(lobby)) {
                    continue;
                }
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

    private void displayMenu() {
        jout.print("=== Menu Join Lobby ===");
        jout.print("1. Applica filtri");
        jout.print("2. Reset filtri");
        jout.print("3. Cerca per nome");
        jout.print("4. Ricarica lista lobby");
        jout.print("0. Torna a Home");
    }

    protected String prompt(String message) {
        jout.print(message);
        return scanner.nextLine().trim();
    }

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

    protected void applyFilters() {
        LinkedHashMap<String, String> typeOptions = new LinkedHashMap<>();
        typeOptions.put("1", "Online");
        typeOptions.put("2", "Live");
        filterType = chooseOption("tipo", typeOptions);

        LinkedHashMap<String, String> durationOptions = new LinkedHashMap<>();
        durationOptions.put("1", "One-Shot");
        durationOptions.put("2", "Campaign");
        filterDuration = chooseOption("durata", durationOptions);

        doFilter();
    }

    private String chooseOption(String filterLabel, LinkedHashMap<String, String> options) {
        jout.print("Scegli filtro per " + filterLabel + " (0 per lasciare vuoto):");
        for (Map.Entry<String, String> entry : options.entrySet()) {
            jout.print(entry.getKey() + ". " + entry.getValue());
        }
        String choice = prompt("Inserisci il numero dell'opzione: ");
        if ("0".equals(choice)) {
            return "";
        }
        return options.getOrDefault(choice, "");
    }

    protected void applySearch() {
        searchQuery = prompt("Inserisci stringa di ricerca: ").toLowerCase();
        doFilter();
    }

    protected void resetFilters() {
        filterType = "";
        filterDuration = "";
        filterNumPlayers = "";
        searchQuery = "";
        doFilter();
    }

    protected void doFilter() {
        List<LobbyBean> result = controller.filterLobbies(filterType, filterDuration, searchQuery);
        filteredLobbies.setAll(result);
    }

    protected void refreshTable() {
        if (controller != null) {
            doFilter();
        }
    }

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