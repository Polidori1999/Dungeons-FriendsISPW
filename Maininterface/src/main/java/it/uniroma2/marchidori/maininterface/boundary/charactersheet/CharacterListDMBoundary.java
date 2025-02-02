package it.uniroma2.marchidori.maininterface.boundary.charactersheet;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CharacterListDMBoundary extends CharacterListBoundary {


    @Override
    public void initialize() {
        super.initialize();
        if (controller == null) {

            return;
        }
        data.addAll(controller.getAllCharacters());
        tableViewCharDelete.setVisible(false);
        tableViewCharButton.setVisible(false);
    }

    @FXML
    void onClickNewCharacter(ActionEvent e) {
        //nothing here
    }

    @FXML
    void onClickYes(ActionEvent e){
        //nothing here
    }

    @FXML
    void onClickNo(ActionEvent e){
        //nothing here
    }



}