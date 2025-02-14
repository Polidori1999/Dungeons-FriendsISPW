package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageLobbyListDMCLIBoundary extends ManageLobbyListPlayerCLIBoundary {

    private UserBean currentUser;
    private ManageLobbyListController controller;
    private List<LobbyBean> data = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("ManageLobbyListDMCLIBoundary");
    private LobbyBean pendingDeleteBean;


    @Override
    public void run() throws IOException {
        if (!initBoundary()) {
            return;
        }
        boolean exit = false;
        while (!exit) {
            displayLobbyList();
            menu();
            String input = prompt("Scegli un'opzione: ");
            exit = manageInput(input);
            jout.print(""); // Riga vuota per separare le iterazioni
        }
    }


    private void menu() {
        jout.print("=== Menu Gestione Lobby ===");
        jout.print("1. Lascia una lobby");
        jout.print("2. Modifica una lobby");
        jout.print("3. Crea una lobby");
        jout.print("4. Ricarica lista lobby");
        jout.print("0. Torna a Home");
    }


    private boolean manageInput(String input) throws IOException {
        switch (input) {
            case "1":
                handleLeaveLobby();
                break;
            case "2":
                handleEditLobby();
                break;
            case "3":
                currentUser.setSelectedLobbyName(null);
                changeScene(SceneNames.MANAGE_LOBBY);
                break;
            case "4":
                refreshLobbyList();
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

    private void handleEditLobby() {
        if (data.isEmpty()) {
            jout.print("Non sei iscritto a nessuna lobby.");
            return;
        }
        String idxStr = prompt("Inserisci il numero della lobby da modificare: ");
        try {
            int index = Integer.parseInt(idxStr);
            if (index < 1 || index > data.size()) {
                jout.print("Indice non valido.");
                return;
            }
            LobbyBean selectedLobby = data.get(index - 1);
            if (!selectedLobby.getOwner().equals(currentUser.getEmail())) {
                jout.print("Questa lobby non è di tua proprietà. Non puoi modificarla.");
                return;
            }
            currentUser.setSelectedLobbyName(selectedLobby.getName());
            changeScene(SceneNames.MANAGE_LOBBY);
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
