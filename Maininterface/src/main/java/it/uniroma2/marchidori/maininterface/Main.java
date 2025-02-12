package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;


public class Main extends Application {


    // Creazione del logger
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    static UserBean tempUser = new UserBean("",null,null,null);



    @Override
    public void start(Stage primaryStage) throws IOException {
        // Avvio della scena JavaFX con il file FXML "login.fxml"
        SceneSwitcher.changeScene(primaryStage, SceneNames.LOGIN, tempUser);
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Seleziona la modalità di esecuzione dell'applicazione:");
            System.out.println("1. CLI");
            System.out.println("2. JavaFX");
            System.out.print("Inserisci il numero corrispondente (1 o 2): ");
            String scelta = scanner.nextLine().trim();
            if ("1".equals(scelta)) {
                System.out.println("Avvio modalità CLI...");
                // Avvio della versione CLI:
                Session.getInstance().setCLI(true);
                SceneSwitcher.changeScene(null, SceneNames.LOGIN, tempUser);

            } else if ("2".equals(scelta)) {
                System.out.println("Avvio modalità JavaFX...");
                // Avvio dell'applicazione JavaFX:
                launch(args);
            }
            System.out.print("");
        }

    }
}
