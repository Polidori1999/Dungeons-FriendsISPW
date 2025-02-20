package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.RegisterController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.Scanner;

public class RegisterCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private RegisterController registerController;
    private Jout jout = new Jout(this.getClass().getSimpleName());

    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        jout.print("=== SCHERMATA DI REGISTRAZIONE ===");

        jout.print("Inserisci email: ");
        String email = scanner.nextLine().trim();

        jout.print("Inserisci password: ");
        String password = scanner.nextLine().trim();

        jout.print("Conferma password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            jout.print("Errore: Tutti i campi devono essere compilati!");
            changeScene(SceneNames.REGISTER);
            return;
        }

        if (!password.equals(confirmPassword)) {
            jout.print("Errore: Le password non coincidono!");
            changeScene(SceneNames.REGISTER);
            return;
        }
        boolean success = registerController.register(email, password);
        if(success){
            try{
                changeScene(SceneNames.LOGIN);
            }catch(IOException e){
                jout.print("Errore nel cambio scena: " + e.getMessage());
            }
        }else{
            jout.print("Errore: Email already exists!");
        }
        jout.print("Registrazione completata per l'email: " + email);
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
        this.registerController = (RegisterController) logicController;
    }
}