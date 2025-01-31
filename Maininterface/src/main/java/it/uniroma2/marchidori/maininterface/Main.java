package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import javafx.application.Application;

import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private UserBean currentUser = new UserBean("123", "Mario", "@lol@", null);

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneSwitcher.changeScene(primaryStage, "login.fxml", currentUser);
    }


    public static void main(String[] args) {
        launch();
    }
}