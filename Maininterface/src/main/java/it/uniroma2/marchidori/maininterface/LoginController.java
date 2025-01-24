package it.uniroma2.marchidori.maininterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class LoginController {
    @FXML
    public void up(ActionEvent e) {
        System.out.println("ups");
    }
    public void center(ActionEvent e) {
        System.out.println("cenret");
    }
    public void right(ActionEvent e) {
        System.out.println("rightd");
    }
    public void left(ActionEvent e) {
        System.out.println("ledft");
    }
    public void down(ActionEvent e) {
        System.out.println("dopwn");
    }
}
