package it.uniroma2.marchidori.maininterface.boundary;

public class CharacterListDMBoundary extends CharacterListBoundary{

    public void initialize() {
        super.initialize();
        newCharacterButton.setVisible(false);
        newCharacterButton.setDisable(true);
        if (controller == null) {
            System.err.println("Errore: controller non inizializzato!");
            return;
        }
        data.addAll(controller.getAllCharacters());
    }

    public void onClickNewCharacter() {
        //null
    }



}
