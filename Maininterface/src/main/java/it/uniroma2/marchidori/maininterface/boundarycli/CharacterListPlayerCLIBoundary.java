package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.CharacterListController;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CharacterListPlayerCLIBoundary extends CharacterListDMCLIBoundary {

    private UserBean currentUser;
    private CharacterListController controller;
    // Lista dei personaggi (CharacterSheetBean) da mostrare
    private List<CharacterSheetBean> data = new ArrayList<>();
    // Eventuale bean in attesa di eliminazione
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
            menu();
            String input = prompt("Scegli un'opzione: ");
            exit = manageInput(input);
            jout.print(""); // Riga vuota per separare le iterazioni
        }
    }

    /**
     * Visualizza il menu delle operazioni disponibili.
     */
    private void menu() {
        jout.print("=== Menu Personaggi ===");
        jout.print("1. Modifica un personaggio");
        jout.print("2. Elimina un personaggio");
        jout.print("3. Scarica un personaggio");
        jout.print("4. Crea un nuovo personaggio");
        jout.print("5. Aggiorna lista personaggi");
        jout.print("0. Torna a Home");
    }

    /**
     * Elabora la scelta dell'utente e richiama la relativa operazione.
     *
     * @param input La scelta inserita dall'utente.
     * @return true se si vuole uscire dalla modalità, false altrimenti.
     * @throws IOException in caso di errori nel cambio scena.
     */
    private boolean manageInput(String input) throws IOException {
        switch (input) {
            case "1":
                handleEditCharacter();
                break;
            case "2":
                handleDeleteCharacter();
                break;
            case "3":
                handleDownloadCharacter();
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
     * Metodo helper per gestire la selezione di un personaggio.
     * Se la lista è vuota, stampa il messaggio di errore passato.
     * Altrimenti, chiede all'utente l'indice e, se valido, esegue l'azione definita nel consumer.
     *
     * @param emptyMessage  Messaggio da mostrare se la lista è vuota.
     * @param promptMessage Messaggio da mostrare per richiedere l'indice.
     * @param action        Azione da eseguire sul personaggio selezionato.
     * @throws IOException se il prompt genera un'eccezione.
     */
    private void handleCharacterSelection(String emptyMessage, String promptMessage,
                                          Consumer<CharacterSheetBean> action) {
        if (data.isEmpty()) {
            jout.print(emptyMessage);
            return;
        }
        String idxStr = prompt(promptMessage);
        try {
            int index = Integer.parseInt(idxStr);
            if (index < 1 || index > data.size()) {
                jout.print("Indice non valido.");
                return;
            }
            action.accept(data.get(index - 1));
        } catch (NumberFormatException e) {
            jout.print("Input non valido.");
        }
    }

    /**
     * Gestisce la modifica di un personaggio selezionato.
     */
    private void handleEditCharacter() {
        handleCharacterSelection("Nessun personaggio da modificare.",
                "Inserisci il numero del personaggio da modificare: ",
                characterSheetBean -> {
                    try {
                        editCharacter(characterSheetBean);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Gestisce il download di un personaggio.
     */
    private void handleDownloadCharacter() {
        handleCharacterSelection("Nessun personaggio disponibile per il download.",
                "Inserisci il numero del personaggio da scaricare: ",
                this::downloadCharacter);
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
