package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageLobbyListPlayerCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private ManageLobbyListController controller;
    private List<LobbyBean> data = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("ManageLobbyListPlayerCLIBoundary");

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
            jout.print(""); // Riga vuota per separare le iterazioni
        }
    }

    private boolean initBoundary() {
        if (currentUser == null) {
            jout.print("ERRORE: Utente non inizializzato.");
            return false;
        }
        refreshLobbyList();
        return true;
    }

    private void displayLobbyList() {
        jout.print("=== Lista Lobby Iscritte ===");
        if (data.isEmpty()) {
            jout.print("Non sei iscritto a nessuna lobby.");
        } else {
            jout.print(String.format("%-3s %-20s %-15s %-10s %-10s", "No", "Nome", "Numero Giocatori", "Durata", "Online"));
            int i = 1;
            for (LobbyBean lobby : data) {
                jout.print(String.format("%-3d %-20s %-15s %-10s %-10s",
                        i, lobby.getName(), lobby.getNumberOfPlayers(), lobby.getDuration(), lobby.getLiveOnline()));
                i++;
            }
        }
    }

    private void displayMenu() {
        jout.print("=== Menu Gestione Lobby ===");
        jout.print("1. Lascia una lobby");
        jout.print("2. Ricarica lista lobby");
        jout.print("3. Torna a Home");
        jout.print("0. Esci");
    }

    private String prompt(String message) {
        jout.print(message);
        return scanner.nextLine().trim();
    }

    private boolean processInput(String input) throws IOException {
        switch (input) {
            case "1":
                handleLeaveLobby();
                break;
            case "2":
                refreshLobbyList();
                jout.print("Lista aggiornata.");
                break;
            case "3":
                changeScene("home.fxml");
                return true;
            case "0":
                jout.print("Uscita dalla gestione lobby.");
                return true;
            default:
                jout.print("Opzione non valida, riprova.");
        }
        return false;
    }

    private void handleLeaveLobby() {
        if (data.isEmpty()) {
            jout.print("Non sei iscritto a nessuna lobby.");
            return;
        }
        String idxStr = prompt("Inserisci il numero della lobby da cui uscire: ");
        try {
            int index = Integer.parseInt(idxStr);
            if (index < 1 || index > data.size()) {
                jout.print("Indice non valido.");
                return;
            }
            LobbyBean lobbyToLeave = data.get(index - 1);
            String conf = prompt("Sei sicuro di voler lasciare la lobby '" + lobbyToLeave.getName() + "'? (y/n): ");
            if (conf.equalsIgnoreCase("y")) {
                processLeave(lobbyToLeave);
                jout.print("Lobby lasciata.");
            } else {
                jout.print("Operazione annullata.");
            }
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        }
    }

    private void processLeave(LobbyBean lobbyToLeave) {
        controller.deleteLobby(lobbyToLeave.getName());
        if (currentUser.getJoinedLobbies() != null) {
            currentUser.getJoinedLobbies().remove(lobbyToLeave);
        }
        refreshLobbyList();
    }

    private void refreshLobbyList() {
        if (controller != null) {
            data = controller.getJoinedLobbies();
            if (currentUser.getJoinedLobbies() == null) {
                currentUser.setJoinedLobbies(new ArrayList<>());
            }
        }
    }

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
        this.controller = (ManageLobbyListController) logicController;
    }
}
