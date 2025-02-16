package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterListController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CharacterListDMCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private CharacterListController controller;
    // Lista dei personaggi (CharacterSheetBean) da mostrare
    private List<CharacterSheetBean> data = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    // Eventuale bean in attesa di eliminazione
    private CharacterSheetBean pendingDeleteBean;
    private final Jout jout = new Jout("CharacterListDMCLIBoundary");

    @Override
    public void run() throws IOException {
        if (currentUser == null) {
            jout.print("ERRORE: Utente non inizializzato.");
            return;
        }
        refreshTable();
        boolean exit = false;
        while (!exit) {
            displayCharacterList();
            displayMenu();
            String input = prompt("Scegli un'opzione: ");
            exit = processInput(input);
            jout.print(""); // Riga vuota per separare le iterazioni
        }
    }

    /**
     * Visualizza in console l'elenco dei personaggi.
     */
    protected void displayCharacterList() {
        jout.print("=== Lista Personaggi ===");
        if (data.isEmpty()) {
            jout.print("Nessun personaggio presente.");
        } else {
            jout.print(String.format("%-3s %-20s", "No", "Nome"));
            int i = 1;
            for (CharacterSheetBean character : data) {
                // Si assume che il bean esponga il nome tramite getInfoBean().getName()
                String name = character.getInfoBean().getName();
                jout.print(String.format("%-3d %-20s", i, name));
                i++;
            }
        }
    }

    /**
     * Visualizza il menu delle operazioni disponibili.
     */
    private void displayMenu() {
        jout.print("=== Menu Personaggi ===");
        jout.print("1. Elimina un personaggio");
        jout.print("2. Aggiorna lista personaggi");
        jout.print("0. Torna a Home");
    }

    /**
     * Richiede un input all'utente.
     */
    protected String prompt(String message) {
        jout.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * Elabora la scelta dell'utente e richiama la relativa operazione.
     *
     * @param input La scelta inserita dall'utente.
     * @return true se si vuole uscire dalla modalità, false altrimenti.
     * @throws IOException in caso di errori nel cambio scena.
     */
    private boolean processInput(String input) throws IOException {
        switch (input) {
            case "1":
                handleDeleteCharacter();
                break;
            case "2":
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
     * Gestisce l'eliminazione di un personaggio.
     */
    protected void handleDeleteCharacter() {
        if (data.isEmpty()) {
            jout.print("Nessun personaggio da eliminare.");
            return;
        }
        String idxStr = prompt("Inserisci il numero del personaggio da eliminare: ");
        try {
            int index = Integer.parseInt(idxStr);
            if (index < 1 || index > data.size()) {
                jout.print("Indice non valido.");
                return;
            }
            pendingDeleteBean = data.get(index - 1);
            String conf = prompt("Vuoi eliminare il personaggio '" + pendingDeleteBean.getInfoBean().getName() + "'? (y/n): ");
            if (conf.equalsIgnoreCase("y")) {
                onConfirmDelete();
                jout.print("Personaggio eliminato.");
            } else {
                onCancelDelete();
                jout.print("Operazione annullata.");
            }
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        }
    }

    /**
     * Conferma l'eliminazione del personaggio, aggiornando la lista e delegando al controller.
     */
    protected void onConfirmDelete() {
        String characterName = pendingDeleteBean.getInfoBean().getName();
        controller.deleteCharacter(characterName);
        data.remove(pendingDeleteBean);
        refreshTable();
    }

    /**
     * Annulla l'operazione di eliminazione.
     */
    protected void onCancelDelete() {
        pendingDeleteBean = null;
    }

    /**
     * Aggiorna la lista dei personaggi leggendo i dati dal currentUser.
     */
    protected void refreshTable() {
        if (currentUser == null) {
            jout.print("ERRORE: currentUser non inizializzato.");
            return;
        }
        if (currentUser.getCharacterSheets() != null) {
            data = new ArrayList<>(currentUser.getCharacterSheets());
        } else {
            data.clear();
        }
        jout.print("Tabella personaggi aggiornata.");
    }


    /**
     * Simula il cambio scena in ambiente CLI.
     */
    protected void changeScene(String fxml) throws IOException {
        jout.print("Cambio scena verso " + fxml + "...");
        SceneSwitcher.changeScene(null, fxml, currentUser);
    }

    // Metodi di "iniezione" delle dipendenze

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (CharacterListController) logicController;
    }
}
