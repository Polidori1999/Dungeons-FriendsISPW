package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageLobbyListPlayerCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    protected UserBean currentUser;
    protected ManageLobbyListController controller;
    protected List<LobbyBean> data = new ArrayList<>();
    protected final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("ManageLobbyListPlayerCLIBoundary");
    protected LobbyBean pendingDeleteBean;

    // Costanti per la stampa tabellare (header + riga)
    private static final String TABLE_HEADER_FORMAT = "%-3s %-20s %-15s %-10s %-10s";
    private static final String TABLE_ROW_FORMAT    = "%-3d %-20s %-15s %-10s %-10s";

    @Override
    public void run() throws IOException {
        if (!initBoundary()) {
            return;
        }
        boolean exit = false;
        while (!exit) {
            displayLobbyList();
            displayMenu();
            String input = prompt("Scegli un'opzione: ");
            exit = processInput(input);
            jout.print(""); // Riga vuota per separare iterazioni
        }
        changeScene(SceneNames.HOME);
    }

    protected boolean initBoundary() {
        if (currentUser == null) {
            jout.print("ERRORE: Utente non inizializzato.");
            return false;
        }
        refreshLobbyList();
        return true;
    }

    protected void displayLobbyList() {
        jout.print("=== Lista Lobby Iscritte ===");
        if (data.isEmpty()) {
            // Se la lista è vuota, stampo e basta
            jout.print("Non sei iscritto a nessuna lobby.");
        } else {
            // Stampo header
            jout.print(String.format(TABLE_HEADER_FORMAT,
                    "No", "Nome", "Num Giocatori", "Durata", "Online"));
            // Stampo le righe
            int i = 1;
            for (LobbyBean lobby : data) {
                jout.print(String.format(TABLE_ROW_FORMAT,
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
        jout.print("=== Menu Gestione Lobby ===");
        jout.print("1. Lascia una lobby");
        jout.print("2. Ricarica lista lobby");
        jout.print("0. Torna a Home");
    }

    protected String prompt(String message) {
        jout.print(message);
        return scanner.nextLine().trim();
    }

    private boolean processInput(String input) {
        switch (input) {
            case "1":
                handleLeaveLobby();
                break;
            case "2":
                refreshLobbyList();
                jout.print("Lista aggiornata.");
                break;
            case "0":
                return true;
            default:
                jout.print("Opzione non valida, riprova.");
        }
        return false;
    }

    protected void handleLeaveLobby() {
        if (checkNoLobby()) {
            return;
        }
        String idxStr = prompt("Inserisci il numero della lobby da cui uscire: ");
        int index;
        try {
            index = Integer.parseInt(idxStr);
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
            return;
        }

        if (index < 1 || index > data.size()) {
            jout.print("Indice non valido.");
            return;
        }
        pendingDeleteBean = data.get(index - 1);
        String conf = prompt("Sei sicuro di voler lasciare la lobby '" + pendingDeleteBean.getName() + "'? (y/n): ");
        if (conf.equalsIgnoreCase("y")) {
            processLeave(pendingDeleteBean);
            jout.print("Lobby lasciata.");
        } else {
            jout.print("Operazione annullata.");
        }
    }

    /**
     * Controlla se la lista 'data' è vuota. In tal caso stampa un messaggio e restituisce true.
     */
    private boolean checkNoLobby() {
        if (data.isEmpty()) {
            jout.print("Non sei iscritto a nessuna lobby.");
            return true;
        }
        return false;
    }

    protected void processLeave(LobbyBean lobbyToLeave) {
        controller.leaveLobby(lobbyToLeave);
        refreshLobbyList();
    }

    protected void refreshLobbyList() {
        if (controller != null) {
            data = controller.getJoinedLobbies();
            if (currentUser.getJoinedLobbies() == null) {
                currentUser.setJoinedLobbies(new ArrayList<>());
            }
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
        this.controller = (ManageLobbyListController) logicController;
    }
}
