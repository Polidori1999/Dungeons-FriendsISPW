package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ConsultRulesController;

import java.util.List;
import java.util.Scanner;

public class ConsultRulesCLIBoundary implements UserAwareInterface, ControllerAwareInterface {

    private UserBean currentUser;
    private ConsultRulesController controller;
    private Jout jout = new Jout(this.getClass().getSimpleName());
    private RuleBookBean pendingBuyBean;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            // Menu principale di CONSULT RULES
            jout.print("=== MENU CONSULT RULES ===");
            jout.print("1. Visualizza RuleBooks");
            jout.print("0. Torna a HOME");
            jout.print("Seleziona un'opzione: ");
            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                jout.print("Input non valido. Riprova.");
                continue;
            }
            if (choice == 0) {
                exit = true;
                break;
            } else if (choice == 1) {
                boolean returnToMenu = false;
                while (!returnToMenu) {
                    refreshList();
                    jout.print("0. Torna al menu CONSULT RULES");
                    jout.print("Seleziona il numero del RuleBook da consultare o comprare: ");
                    String listInput = scanner.nextLine().trim();
                    int listChoice;
                    try {
                        listChoice = Integer.parseInt(listInput);
                    } catch (NumberFormatException e) {
                        jout.print("Input non valido. Riprova.");
                        continue;
                    }
                    if (listChoice == 0) {
                        returnToMenu = true;
                        break;
                    }
                    List<RuleBookBean> ruleBooks = controller.getAllRuleBooks();
                    if (listChoice < 1 || listChoice > ruleBooks.size()) {
                        jout.print("Scelta non valida. Riprova.");
                        continue;
                    }
                    RuleBookBean selected = ruleBooks.get(listChoice - 1);
                    if (selected.isObtained()) {
                        // Se il manuale è già posseduto, simuliamo l'apertura del file
                        jout.print("Apertura del RuleBook: " + selected.getRulesBookName());
                        controller.openFileIfExists(selected.getPath());
                    } else {
                        // Gestione dell'azione di acquisto tramite il metodo dedicato
                        handleBuyAction(selected, scanner);
                    }
                    jout.print(""); // Riga vuota per separare le iterazioni
                    ruleBooks = controller.getAllRuleBooks();
                }
            } else {
                jout.print("Scelta non valida. Riprova.");
            }
        }
        jout.print("Ritorno alla schermata HOME...");
        // Nota: lo scanner non viene chiuso qui se condiviso con altre parti dell'applicazione
    }

    /**
     * Metodo che recupera e visualizza l'elenco aggiornato dei RuleBooks.
     */
    private void refreshList() {
        List<RuleBookBean> updatedList = controller.getAllRuleBooks();
        if (updatedList == null || updatedList.isEmpty()) {
            jout.print("Nessun RuleBook disponibile.");
        } else {
            jout.print("=== ELENCO RULEBOOKS AGGIORNATO ===");
            for (int i = 0; i < updatedList.size(); i++) {
                RuleBookBean rb = updatedList.get(i);
                String status = rb.isObtained() ? "OWNED" : "NOT OWNED";
                jout.print((i + 1) + ". " + rb.getRulesBookName() + " [" + status + "]");
            }
        }
    }

    /**
     * Gestisce l'azione di acquisto di un RuleBook.
     * Visualizza un menu di conferma (Yes/No) e, in base alla scelta,
     * chiama onConfirm oppure onCancel. Al termine, refresh della lista.
     *
     * @param bean    Il RuleBookBean da acquistare.
     * @param scanner Lo scanner per leggere l'input dell'utente.
     */
    private void handleBuyAction(RuleBookBean bean, Scanner scanner) {
        // Salviamo il bean in pendingBuyBean per l'operazione di acquisto.
        pendingBuyBean = bean;

        // Visualizza il prompt di conferma
        jout.print("Vuoi comprare il libro: " + bean.getRulesBookName() + "? (S/N)");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("S") || input.equalsIgnoreCase("Yes")) {
            onConfirm();
            jout.print("Hai acquistato il RuleBook: " + bean.getRulesBookName());
        } else {
            onCancel();
            jout.print("Acquisto annullato.");
        }

        // Refresh della lista dei RuleBooks
        refreshList();
    }

    /**
     * Chiamato quando si annulla l'operazione di acquisto.
     */
    private void onCancel() {
        pendingBuyBean = null;
    }

    /**
     * Chiamato quando si conferma l'acquisto.
     * Aggiorna il bean selezionato (impostando obtained=true) e aggiorna il repository.
     */
    private void onConfirm() {
        if (pendingBuyBean != null) {
            pendingBuyBean.setObtained(true);
            // Aggiorna il repository tramite il controller
            controller.updateRuleBook(pendingBuyBean);
            pendingBuyBean = null;
        }
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ConsultRulesController) logicController;
    }
}
