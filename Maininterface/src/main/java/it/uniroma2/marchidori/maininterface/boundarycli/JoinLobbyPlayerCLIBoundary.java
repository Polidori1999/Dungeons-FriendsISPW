package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;

import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class JoinLobbyPlayerCLIBoundary extends JoinLobbyDMCLIBoundary{

    private UserBean currentUser;
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
    }


    /**
     * Visualizza il menu delle operazioni disponibili in modalità CLI.
     */
    private void menu() {
        jout.print("=== Menu Join Lobby ===");
        jout.print("1. Applica filtri");
        jout.print("2. Reset filtri");
        jout.print("3. Cerca per nome");
        jout.print("4. Unisciti a una lobby");
        jout.print("5. Ricarica lista lobby");
        jout.print("0. Torna a Home");
    }


    /**
     * Elabora l'input utente e delega l'esecuzione delle operazioni.
     *
     * @param input La scelta dell'utente.
     * @return true se si vuole uscire dalla modalità CLI, false altrimenti.
     * @throws IOException in caso di errori nel cambio scena.
     */
    private boolean manageInput(String input) throws IOException {
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
                controller.addLobby(lobbyToJoin);
                refreshTable();
                resetFilters();
            } else {
                jout.print("Operazione annullata.");
            }
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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