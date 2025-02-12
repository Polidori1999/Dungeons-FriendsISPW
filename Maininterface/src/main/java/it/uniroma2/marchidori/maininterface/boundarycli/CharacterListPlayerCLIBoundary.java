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

public class CharacterListPlayerCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private CharacterListController controller;
    // Lista dei personaggi (CharacterSheetBean) da mostrare
    private List<CharacterSheetBean> data = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    // Eventuale bean in attesa di eliminazione
    private CharacterSheetBean pendingDeleteBean;
    private final Jout jout = new Jout("CharacterListPlayerCLIBoundary");

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
    private void displayCharacterList() {
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
        jout.print("1. Modifica un personaggio");
        jout.print("2. Elimina un personaggio");
        jout.print("3. Scarica un personaggio");
        jout.print("4. Crea un nuovo personaggio");
        jout.print("5. Aggiorna lista personaggi");
        jout.print("0. Torna a Home");

    }

    /**
     * Richiede un input all'utente.
     */
    private String prompt(String message) {
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
                handleEditCharacter();
                break;
            case "2":
                handleDeleteCharacter();
                break;
            case "3":
                handleDownloadCharacter(); //domani
                break;
            case "4":
                handleNewCharacter();
                break;
            case "5":
                refreshTable();
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

    /**
     * Gestisce la modifica di un personaggio selezionato.
     */
    private void handleEditCharacter() throws IOException {
        if (data.isEmpty()) {
            jout.print("Nessun personaggio da modificare.");
            return;
        }
        String idxStr = prompt("Inserisci il numero del personaggio da modificare: ");
        try {
            int index = Integer.parseInt(idxStr);
            if (index < 1 || index > data.size()) {
                jout.print("Indice non valido.");
                return;
            }
            CharacterSheetBean characterToEdit = data.get(index - 1);
            editCharacter(characterToEdit);
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        }
    }

    /**
     * Imposta il personaggio da modificare e simula il cambio scena verso l'editor.
     */
    private void editCharacter(CharacterSheetBean characterSheetBean) throws IOException {
        // Imposta il nome del personaggio selezionato (usato per individuare il bean nell'editor)
        currentUser.setSelectedLobbyName(characterSheetBean.getInfoBean().getName());
        jout.print("Modifica del personaggio '" + characterSheetBean.getInfoBean().getName() + "'.");
        changeScene(SceneNames.CHARACTER_SHEET);
    }

    /**
     * Gestisce l'eliminazione di un personaggio.
     */
    private void handleDeleteCharacter() {
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
    private void onConfirmDelete() {
        String characterName = pendingDeleteBean.getInfoBean().getName();
        controller.deleteCharacter(characterName);
        data.remove(pendingDeleteBean);
        refreshTable();
    }

    /**
     * Annulla l'operazione di eliminazione.
     */
    private void onCancelDelete() {
        pendingDeleteBean = null;
    }

    /**
     * Gestisce il download di un personaggio.
     */
    private void handleDownloadCharacter() {
        if (data.isEmpty()) {
            jout.print("Nessun personaggio disponibile per il download.");
            return;
        }
        String idxStr = prompt("Inserisci il numero del personaggio da scaricare: ");
        try {
            int index = Integer.parseInt(idxStr);
            if (index < 1 || index > data.size()) {
                jout.print("Indice non valido.");
                return;
            }
            CharacterSheetBean characterToDownload = data.get(index - 1);
            downloadCharacter(characterToDownload);
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        }
    }

    /**
     * Simula il processo di download del personaggio.
     */
    private void downloadCharacter(CharacterSheetBean bean) {
        jout.print("Avvio download del personaggio '" + bean.getInfoBean().getName() + "'...");
        // Simulazione di download tramite stampa progressiva
        for (int progress = 0; progress <= 100; progress += 20) {
            try {
                Thread.sleep(500); // Simula attesa
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            jout.print("Download: " + progress + "%");
        }
        jout.print("Download completato per '" + bean.getInfoBean().getName() + "'.");
    }

    /**
     * Gestisce la creazione di un nuovo personaggio.
     */
    private void handleNewCharacter() throws IOException {
        currentUser.setSelectedLobbyName(null);
        jout.print("Creazione di un nuovo personaggio.");
        changeScene(SceneNames.CHARACTER_SHEET);
    }

    /**
     * Aggiorna la lista dei personaggi leggendo i dati dal currentUser.
     */
    public void refreshTable() {
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
    private void changeScene(String fxml) throws IOException {
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

    // Metodi helper per la gestione della "tabella" in CLI

    public void addNewCharacterBean(CharacterSheetBean newBean) {
        if (newBean != null) {
            jout.print("Aggiunta del nuovo personaggio: " + newBean.getInfoBean().getName());
            data.add(newBean);
        } else {
            jout.print("ERRORE: nuovo personaggio è null.");
        }
    }

    public void addCharacterToTable(CharacterSheetBean character) {
        data.add(character);
        jout.print("Personaggio aggiunto: " + character.getInfoBean().getName());
    }

    public void updateExistingCharacterInTable(CharacterSheetBean updatedCharacter) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getInfoBean().getName().equals(updatedCharacter.getInfoBean().getName())) {
                data.set(i, updatedCharacter);
                break;
            }
        }
        jout.print("Personaggio aggiornato: " + updatedCharacter.getInfoBean().getName());
    }
}
