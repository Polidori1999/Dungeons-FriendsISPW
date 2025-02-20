package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ConsultRulesController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConsultRulesCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private ConsultRulesController controller;
    private final Jout jout = new Jout(this.getClass().getSimpleName());
    private RuleBookBean pendingBuyBean;
    private UserBean currentUser;

    @Override
    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            exit = processMainMenu(scanner);
        }
        jout.print("Ritorno alla schermata HOME...");
        changeScene();

    }

    private boolean processMainMenu(Scanner scanner) {
        int choice;
        do {
            displayMainMenu();
            choice = readChoice(scanner, "Seleziona un'opzione: ");
            if (choice != 0 && choice != 1) {
                jout.print("Scelta non valida. Riprova.");
            }
        } while (choice != 0 && choice != 1);

        if (choice == 0) {
            return true;
        } else {
            showRuleBooksMenu(scanner);
            return false;
        }
    }


    private void displayMainMenu() {
        jout.print("=== MENU CONSULT RULES ===");
        jout.print("1. Visualizza RuleBooks");
        jout.print("0. Torna a HOME");
    }


    private void showRuleBooksMenu(Scanner scanner) {
        boolean returnToMenu = false;
        List<RuleBookBean> ruleBooks = controller.getAllRuleBooks();
        while (!returnToMenu) {
            refreshList();
            int listChoice = readChoice(scanner,
                    "0. Torna al menu CONSULT RULES\nSeleziona il numero del RuleBook da consultare o comprare: ");

            if (listChoice == 0) {
                returnToMenu = true;
            } else if (listChoice < 1 || listChoice > ruleBooks.size()) {
                jout.print("Scelta non valida. Riprova.");
            } else {
                processRuleBookSelection(ruleBooks.get(listChoice - 1), scanner);
                jout.print(""); // Riga vuota per separare le iterazioni
            }
        }

    }

    private void processRuleBookSelection(RuleBookBean selected, Scanner scanner) {
        if (selected.isObtained()) {
            jout.print("Apertura del RuleBook: " + selected.getRulesBookName());
            controller.openFileIfExists(selected.getPath());
        } else {
            handleBuyAction(selected, scanner);
        }
    }


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


    private void handleBuyAction(RuleBookBean bean, Scanner scanner) {
        pendingBuyBean = bean;
        jout.print("Vuoi comprare il libro: " + bean.getRulesBookName() + "? (Y/N)");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("Yes")) {
            onConfirm();
            jout.print("Hai acquistato il RuleBook: " + bean.getRulesBookName());
        } else {
            onCancel();
            jout.print("Acquisto annullato.");
        }
        refreshList();
    }

    private void onCancel() {
        pendingBuyBean = null;
    }

    private void onConfirm() {
        if (pendingBuyBean != null) {
            double price = 0.01;
            controller.startPayPalPayment(price);

            pendingBuyBean.setObtained(true);

            pendingBuyBean = null;
            refreshList();
        }
    }

    private void changeScene() throws IOException {
        jout.print("Cambio scena verso: " + SceneNames.HOME);
        SceneSwitcher.changeScene(null, SceneNames.HOME, currentUser);
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