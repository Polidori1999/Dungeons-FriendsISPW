package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.LoginController;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private LoginController loginController;
    private final Jout jout = new Jout(this.getClass().getSimpleName());

    private static final String GUEST_EMAIL = "guest@example.com";


    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        jout.print("Benvenuto nel sistema di login (CLI)");
        while (!exit) {
            mostraMenu();
            jout.print("Scegli un'opzione: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    eseguiLogin(scanner);
                    break;
                case "2":
                    eseguiCreateAccount();
                    break;
                case "0":
                    jout.print("Uscita dal programma.");
                    exit = true;
                    System.exit(0);
                    break;
                default:
                    jout.print("Opzione non valida, riprova.");

                    break;
            }
            jout.print("");
        }
    }

    private void mostraMenu() {
        jout.print("=== MENU DI LOGIN ===");
        jout.print("1. Login");
        jout.print("2. Crea Account");
        jout.print("0. Esci");
    }

    private void eseguiLogin(Scanner scanner) throws IOException {
        jout.print("Inserisci email: ");
        String email = scanner.nextLine().trim();
        jout.print("Inserisci password: ");
        String password = scanner.nextLine().trim();
        User authenticatedUser = loginController.login(email, password);
        if (authenticatedUser != null) {
            currentUser = Converter.convert(authenticatedUser);
            changeScene(SceneNames.HOME);
        } else {
            jout.print(">>> ERRORE: Login fallito. UserBean è NULL!");
        }
    }

    private void eseguiCreateAccount() throws IOException {
        if (currentUser == null) {
            jout.print("Creazione di un UserBean temporaneo per la registrazione.");
            setCurrentUser(new UserBean(
                    "temp@example.com",
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>())
            );
        }
        jout.print("Passaggio alla schermata di registrazione (simulazione).");
        changeScene(SceneNames.REGISTER);
    }

    private void changeScene(String sceneName) throws IOException {
        jout.print("Cambio scena verso: " + sceneName);
        SceneSwitcher.changeScene(null, sceneName, currentUser);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.loginController = (LoginController) logicController;
    }
}