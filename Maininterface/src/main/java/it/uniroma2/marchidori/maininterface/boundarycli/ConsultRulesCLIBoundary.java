package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ConsultRulesController;

import java.util.List;
import java.util.Scanner;

public class ConsultRulesCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private ConsultRulesController controller;
    private Jout jout = new Jout(this.getClass().getSimpleName());
    private RuleBookBean pendingBuyBean;

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ConsultRulesController) logicController;
    }

    /**
     * Punto di ingresso per la modalità CLI di Consult Rules.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            exit = processMainMenu(scanner);
        }
        jout.print("Ritorno alla schermata HOME...");
    }

    /**
     * Visualizza il menu principale e processa la scelta dell'utente.
     *
     * @param scanner lo Scanner per l'input
     * @return true se l'utente sceglie di tornare alla home, false altrimenti.
     */
    private boolean processMainMenu(Scanner scanner) {
        displayMainMenu();
        int choice = readChoice(scanner, "Seleziona un'opzione: ");
        switch (choice) {
            case 0:
                return true;
            case 1:
                showRuleBooksMenu(scanner);
                break;
            default:
                jout.print("Scelta non valida. Riprova.");
        }
        return false;
    }

    /**
     * Visualizza il menu principale di Consult Rules.
     */
    private void displayMainMenu() {
        jout.print("=== MENU CONSULT RULES ===");
        jout.print("1. Visualizza RuleBooks");
        jout.print("0. Torna a HOME");
    }

    /**
     * Gestisce il menu per la consultazione/acquisto dei RuleBooks.
     *
     * @param scanner lo Scanner per l'input
     */
    private void showRuleBooksMenu(Scanner scanner) {
        boolean returnToMenu = false;
        List<RuleBookBean> ruleBooks = controller.getAllRuleBooks();
        while (!returnToMenu) {
            refreshList();
            int listChoice = readChoice(scanner, "0. Torna al menu CONSULT RULES\n" +
                    "Seleziona il numero del RuleBook da consultare o comprare: ");
            if (listChoice == 0) {
                returnToMenu = true;
                continue;
            }else if(listChoice < 1 || listChoice > controller.getAllRuleBooks().size()){
                jout.print("Scelta non valida. Riprova.");
            }
            RuleBookBean selected = ruleBooks.get(listChoice - 1);
            processRuleBookSelection(selected, scanner);
            jout.print(""); // Riga vuota per separare le iterazioni
        }
    }

    /**
     * Processa la selezione di un singolo RuleBook.
     *
     * @param selected il RuleBook selezionato
     * @param scanner  lo Scanner per l'input
     */
    private void processRuleBookSelection(RuleBookBean selected, Scanner scanner) {
        if (selected.isObtained()) {
            jout.print("Apertura del RuleBook: " + selected.getRulesBookName());
            controller.openFileIfExists(selected.getPath());
        } else {
            handleBuyAction(selected, scanner);
        }
    }

    /**
     * Legge e converte l'input dell'utente in un intero.
     *
     * @param scanner lo Scanner per l'input
     * @param prompt  il messaggio da visualizzare all'utente
     * @return il numero scelto, oppure -1 se l'input non è valido
     */
    private int readChoice(Scanner scanner, String prompt) {
        jout.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            jout.print("Input non valido. Riprova.");
            return -1;
        }
    }

    /**
     * Recupera e visualizza l'elenco aggiornato dei RuleBooks.
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
     *
     * @param bean    il RuleBookBean da acquistare
     * @param scanner lo Scanner per l'input dell'utente
     */
    private void handleBuyAction(RuleBookBean bean, Scanner scanner) {
        pendingBuyBean = bean;
        jout.print("Vuoi comprare il libro: " + bean.getRulesBookName() + "? (S/N)");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("S") || input.equalsIgnoreCase("Yes")) {
            onConfirm();
            jout.print("Hai acquistato il RuleBook: " + bean.getRulesBookName());
        } else {
            onCancel();
            jout.print("Acquisto annullato.");
        }
        refreshList();
    }

    /**
     * Annulla l'operazione di acquisto.
     */
    private void onCancel() {
        pendingBuyBean = null;
    }

    /**
     * Conferma l'acquisto, aggiornando il bean e il repository.
     */
    private void onConfirm() {
        if (pendingBuyBean != null) {
            pendingBuyBean.setObtained(true);
            controller.updateRuleBook(pendingBuyBean);
            pendingBuyBean = null;
        }
    }
}
