package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.CharacterInfo;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.CharacterStats;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CharacterListController implements UserAwareInterface {
    private UserBean currentUser;

    public CharacterListController() {}


    public List<CharacterSheetBean> getAllCharacters() {
        List<CharacterSheetBean> beans = new ArrayList<>();
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            for (CharacterSheet cs : currentUser.getCharacterSheets()) {
                beans.add(entityToBean(cs)); // **Converte sempre i dati aggiornati**
            }
        }
        return beans;
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

    /**
     * Converte da Bean "spezzato" a Entity pura.
     */
    private CharacterSheet beanToEntity(CharacterSheetBean bean) {
        CharacterInfoBean infoBean = bean.getInfoBean();
        CharacterStatsBean statsBean = bean.getStatsBean();

        CharacterInfo infoEntity = new CharacterInfo(
                infoBean.getName(),
                infoBean.getRace(),
                infoBean.getAge(),
                infoBean.getClasse(),
                infoBean.getLevel()
        );
        CharacterStats statsEntity = new CharacterStats(
                statsBean.getStrength(),
                statsBean.getDexterity(),
                statsBean.getIntelligence(),
                statsBean.getWisdom(),
                statsBean.getCharisma(),
                statsBean.getConstitution()
        );
        return new CharacterSheet(infoEntity, statsEntity);
    }


    public void deleteCharacter(String characterName) {
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            for (int i = 0; i < currentUser.getCharacterSheets().size(); i++) {
                if (currentUser.getCharacterSheets().get(i).getName().equals(characterName)) {
                    currentUser.getCharacterSheets().remove(i);
                    System.out.println(">>> DEBUG: Personaggio eliminato dallo UserBean: " + characterName);
                    return;
                }
            }
            System.err.println(">>> ERRORE: Nessun personaggio trovato con il nome: " + characterName);
        } else {
            System.err.println(">>> ERRORE: currentUser o lista personaggi NULL in deleteCharacter()");
        }
    }

    ///////down
    public CharacterSheetDownloadTask getDownloadTask(CharacterSheetBean bean) {
        try {
            // Ottieni la cartella di download dinamica
            String userHome = System.getProperty("user.home");
            String downloadFolder = Paths.get(userHome, "Downloads").toString();

            // Crea il nome del file
            String fileName = "character_" + bean.getInfoBean().getName() + ".txt";
            String destinationPath = Paths.get(downloadFolder, fileName).toString();

            System.out.println(">>> DEBUG: Percorso di download: " + destinationPath);

            // Crea e restituisci il task di download
            return new CharacterSheetDownloadTask(bean, destinationPath);

        } catch (Exception e) {
            System.err.println("Errore durante il download: " + e.getMessage());
            return null;
        }
    }


    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
