package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.login.RegisterBoundary;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.LoginController;
import it.uniroma2.marchidori.maininterface.control.RegisterController;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginCLIBoundary implements UserAwareInterface, ControllerAwareInterface {

    private UserBean currentUser;
    private LoginController loginController;
    private Jout jout = new Jout(this.getClass().getSimpleName());

    private static final String GUEST_EMAIL = "guest@example.com";

    public void run() {
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
                    eseguiGuest();
                    break;
                case "3":
                    eseguiCreateAccount();
                    break;
                case "4":
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
        scanner.close();
    }

    private void mostraMenu() {
        jout.print("=== MENU DI LOGIN ===");
        jout.print("1. Login");
        jout.print("2. Accesso come Guest");
        jout.print("3. Crea Account");
        jout.print("4. Esci");
    }

    private void eseguiLogin(Scanner scanner) {
        jout.print("Inserisci email: ");
        String email = scanner.nextLine().trim();

        jout.print("Inserisci password: ");
        String password = scanner.nextLine().trim();

        UserBean authenticatedUser = loginController.login(email, password);
        if (authenticatedUser != null) {
            currentUser = authenticatedUser;
            jout.print("Login avvenuto con successo. Benvenuto " + currentUser.getEmail() + "!");
            changeScene("HOME", currentUser);
        } else {
            jout.print("Errore: Wrong email or password!");
        }
    }

    private void eseguiGuest() {
        currentUser = new UserBean(
                GUEST_EMAIL,
                "guest",  // password fittizia
                RoleEnum.GUEST,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        jout.print("Accesso come Guest effettuato.");
        Session.getInstance().setCurrentUser(Converter.userBeanToEntity(currentUser));
        changeScene("HOME", currentUser);
    }

    private void eseguiCreateAccount() {
        if (currentUser == null) {
            jout.print("Creazione di un UserBean temporaneo per la registrazione.");
            currentUser = new UserBean(
                    "temp@example.com",
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            changeScene("REGISTER", currentUser);
        }
        jout.print("Passaggio alla schermata di registrazione (simulazione).");
        changeScene("REGISTER", currentUser);
    }

    private void changeScene(String sceneName, UserBean user) {
        jout.print("Cambio scena verso: " + sceneName);
        jout.print("Utente attuale: " + user.getEmail());

        if ("HOME".equalsIgnoreCase(sceneName)) {
            HomeCLIBoundary homeBoundary = new HomeCLIBoundary();
            homeBoundary.setCurrentUser(user);
            homeBoundary.run();
        } else if ("REGISTER".equalsIgnoreCase(sceneName)) {
            RegisterCLIBoundary registerBoundary = new RegisterCLIBoundary();
            registerBoundary.setCurrentUser(user);
            registerBoundary.setLogicController(new RegisterController());
            registerBoundary.run();
            jout.print("Schermata di registrazione (CLI) non implementata.");
        } else {
            jout.print("Scena non riconosciuta.");
        }
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
