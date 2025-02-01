package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

public class CharacterListDMBoundary extends CharacterListBoundary {
    @Override
    public void initialize() {
        super.initialize();
        newCharacterButton.setVisible(false);
        newCharacterButton.setDisable(true);
        if (controller == null) {

            return;
        }
        data.addAll(controller.getAllCharacters());
    }

    public void onClickNewCharacter() {
        //null
    }



}
