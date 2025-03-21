package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

import it.uniroma2.marchidori.maininterface.entity.*;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CharacterListController implements UserAwareInterface {
    //entita user presa dalla sessione
    private final User currentEntity = Session.getInstance().getCurrentUser();
    private UserBean currentUser;
    private final Jout jout = new Jout("CharacterListController");


    private static final Logger logger = Logger.getLogger(CharacterListController.class.getName());

    public CharacterListController() {
        // empty
    }


    public void deleteCharacter(String characterName) {
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            for (int i = 0; i < currentUser.getCharacterSheets().size(); i++) {
                if (currentUser.getCharacterSheets().get(i).getInfoBean().getName().equals(characterName)) {
                    currentUser.getCharacterSheets().remove(i);
                    currentEntity.getCharacterSheets().remove(i);
                    UserDAO dao = Session.getInstance().getUserDAO();
                    dao.updateUsersEntityData(currentEntity);
                    return;
                }
            }
            logger.log(Level.SEVERE, () -> "ERRORE: Nessun personaggio trovato con il nome: " + characterName);
        } else {
            logger.severe("ERRORE: currentUser o lista personaggi NULL in deleteCharacter()");
        }
    }

    public CharacterSheetDownloadTask getDownloadTask(CharacterSheetBean bean) {
        try {
            // Ottieni la cartella di download dinamica
            String userHome = System.getProperty("user.home");
            String downloadFolder = Paths.get(userHome, "Downloads").toString();

            // Crea il nome del file
            String fileName = "character_" + bean.getInfoBean().getName() + ".txt";
            String destinationPath = Paths.get(downloadFolder, fileName).toString();

            logger.info(() -> ">>> DEBUG: Percorso di download: " + destinationPath);

            // Crea e restituisci il task di download
            return new CharacterSheetDownloadTask(bean, destinationPath);

        } catch (Exception e) {
            logger.severe("Errore durante il download: " + e.getMessage());
            return null;
        }
    }

    public List<CharacterSheetBean> getCharacterSheets() {
        List<CharacterSheetBean> beans = new ArrayList<>();

        // Verifichiamo che currentEntity e la sua lista non siano null
        if (currentEntity != null && currentEntity.getCharacterSheets() != null) {
            for (CharacterSheet cs : currentEntity.getCharacterSheets()) {
                // Converte la entity CharacterSheet in CharacterSheetBean
                CharacterSheetBean bean = Converter.characterSheetEntityToBean(cs);
                beans.add(bean);
            }
        } else {
            logger.severe(">>> ERRORE: currentEntity o la sua lista di CharacterSheet è null in getCharacterSheets()");
        }
        jout.print("getCharacterSheets restituisce " + beans.size() + " elementi.");
        return beans;
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
