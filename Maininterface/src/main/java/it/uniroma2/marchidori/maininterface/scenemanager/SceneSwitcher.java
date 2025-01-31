package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserBoundary;
import it.uniroma2.marchidori.maininterface.boundary.UserDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.UserPlayerBoundary;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SceneSwitcher {

    // Mappa che associa DM -> UserDMBoundary e PLAYER -> UserPlayerBoundary
    private static final Map<RoleEnum, Class<? extends UserBoundary>> ROLE_TO_BOUNDARY = new HashMap<>();

    static {
        ROLE_TO_BOUNDARY.put(RoleEnum.DM, UserDMBoundary.class);
        ROLE_TO_BOUNDARY.put(RoleEnum.PLAYER, UserPlayerBoundary.class);
    }

    private SceneSwitcher() {
        // costruttore privato per evitare istanziamenti
    }

    /**
     * Cambia la scena caricando il file FXML specificato,
     * creando il controller in base al ruolo di `currentUser`.
     */
    public static void changeScene(Stage currentStage, String fxmlPath, UserBean currentUser) throws IOException {
        // 1) Scopriamo il ruolo
        RoleEnum role = currentUser.getRoleBehavior();

        // 2) Troviamo la classe del controller corrispondente
        Class<? extends UserBoundary> boundaryClass = ROLE_TO_BOUNDARY.get(role);
        if (boundaryClass == null) {
            throw new IllegalArgumentException("Nessun controller definito per il ruolo: " + role);
        }

        // 3) Istanziamo il controller via reflection
        UserBoundary controller;
        try {
            controller = boundaryClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Impossibile creare il controller per ruolo: " + role, e);
        }

        // 4) Passiamo l'utente
        controller.setCurrentUser(currentUser);

        // 5) Carichiamo l'FXML (senza fx:controller nel file)
        FXMLLoader loader = new FXMLLoader(
                SceneSwitcher.class.getResource("/it/uniroma2/marchidori/maininterface/" + fxmlPath)
        );
        if(fxmlPath.equals("user.fxml")) {
            loader.setController(controller);
        }
        Parent root = loader.load();

        // 6) Impostiamo la nuova scena
        Scene newScene = new Scene(root);
        currentStage.setScene(newScene);
        currentStage.show();
    }
}