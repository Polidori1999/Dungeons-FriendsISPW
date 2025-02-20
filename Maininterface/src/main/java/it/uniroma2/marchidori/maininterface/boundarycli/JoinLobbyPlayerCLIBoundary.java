package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.control.Converter;

import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.FXCollections;

import java.io.IOException;

public class JoinLobbyPlayerCLIBoundary extends JoinLobbyDMCLIBoundary{

    private final Jout jout = new Jout("JoinLobbyPlayerCLIBoundary");


    @Override
    public void run() throws IOException {
        if (currentUser == null) {
            jout.print("ERRORE: Utente non inizializzato.");
            return;
        }
        filteredLobbies = FXCollections.observableArrayList(Converter.convertLobbyListEntityToBean(controller.getLobbies()));
        refreshTable();
        boolean exit = false;
        while (!exit) {
            displayLobbyList();
            menu();
            String input = prompt("Scegli un'opzione: ");
            exit = manageInput(input);
            jout.print(""); // Riga vuota per separare le iterazioni
        }
        jout.print(currentUser.getRoleBehavior().getRoleName());
        changeScene(SceneNames.HOME);
    }

    private void menu() {
        jout.print("=== Menu Join Lobby ===");
        jout.print("1. Applica filtri");
        jout.print("2. Reset filtri");
        jout.print("3. Cerca per nome");
        jout.print("4. Unisciti a una lobby");
        jout.print("5. Ricarica lista lobby");
        jout.print("0. Torna a Home");
    }


    private boolean manageInput(String input)  {
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
            case "0":
                return true;
            default:
                jout.print("Opzione non valida, riprova.");
        }
        return false;
    }


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
            String validationErrors = controller.validate(lobbyToJoin);
            if (!validationErrors.isEmpty()) {
                jout.print(validationErrors);
                return;
            }
            String conf = prompt("Vuoi unirti alla lobby '" + lobbyToJoin.getName() + "'? (y/n): ");
            if (conf.equalsIgnoreCase("y")) {
                controller.addLobby(lobbyToJoin);
                refreshTable();
                resetFilters();
            } else {
                jout.print("Operazione annullata.");
            }
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        }
    }
}