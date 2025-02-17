package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;

public class ManageLobbyListDMCLIBoundary extends ManageLobbyListPlayerCLIBoundary {

    private final Jout jout = new Jout("ManageLobbyListDMCLIBoundary");


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
        changeScene(SceneNames.HOME);
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
                handleCreateLobby();
                break;
            case "4":
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


    /**
     * Gestisce la creazione di un nuovo personaggio.
     */
    private void handleCreateLobby() throws IOException {
        currentUser.setSelectedLobbyName(null);
        jout.print("Creazione di un nuovo personaggio.");
        changeScene(SceneNames.MANAGE_LOBBY);
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
        } catch (NumberFormatException | IOException e) {
            jout.print("Input non valido.");
        }
    }
}
