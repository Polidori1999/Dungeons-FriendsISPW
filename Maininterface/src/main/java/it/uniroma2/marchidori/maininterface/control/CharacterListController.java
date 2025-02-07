package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.*;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.control.CharacterSheetController.getCharacterSheetDownloadTask;

public class CharacterListController implements UserAwareInterface {
    private final User currentEntity = Session.getCurrentUser();
    private UserBean currentUser;

    private static final Logger logger = Logger.getLogger(CharacterListController.class.getName());

    public CharacterListController() {
        // empty
    }

    private CharacterSheetBean entityToBean(CharacterSheet cs) {
        // Crea la parte "info"
        CharacterInfoBean infoBean = new CharacterInfoBean(
                cs.getName(),
                cs.getRace(),
                cs.getAge(),
                cs.getClasse(),
                cs.getLevel()
        );

        // Crea la parte "ability scores"
        CharacterStatsBean abilityBean = new CharacterStatsBean(
                cs.getStrength(),
                cs.getDexterity(),
                cs.getIntelligence(),
                cs.getWisdom(),
                cs.getCharisma(),
                cs.getConstitution()
        );

        // Infine crea il bean complessivo
        return new CharacterSheetBean(infoBean, abilityBean);
    }


    public void deleteCharacter(String characterName) {
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            for (int i = 0; i < currentUser.getCharacterSheets().size(); i++) {
                if (currentUser.getCharacterSheets().get(i).getInfoBean().getName().equals(characterName)) {
                    currentUser.getCharacterSheets().remove(i);
                    currentEntity.getCharacterSheets().remove(i);
                    logger.info(() -> ">>> DEBUG: Personaggio eliminato dallo UserBean: " + characterName);
                    return;
                }
            }
            logger.log(Level.SEVERE, () -> ">>> ERRORE: Nessun personaggio trovato con il nome: " + characterName);
        } else {
            logger.severe(">>> ERRORE: currentUser o lista personaggi NULL in deleteCharacter()");
        }
    }

    public CharacterSheetDownloadTask getDownloadTask(CharacterSheetBean bean) {
        return getCharacterSheetDownloadTask(bean, logger);
    }

    public List<CharacterSheetBean> getCharacterSheets() {
        List<CharacterSheetBean> beans = new ArrayList<>();

        // Verifichiamo che currentEntity e la sua lista non siano null
        if (currentEntity != null && currentEntity.getCharacterSheets() != null) {
            for (CharacterSheet cs : currentEntity.getCharacterSheets()) {
                // Converte la entity CharacterSheet in CharacterSheetBean
                CharacterSheetBean bean = entityToBean(cs);
                beans.add(bean);
            }
        } else {
            logger.severe(">>> ERRORE: currentEntity o la sua lista di CharacterSheet è null in getCharacterSheets()");
        }

        return beans;
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
