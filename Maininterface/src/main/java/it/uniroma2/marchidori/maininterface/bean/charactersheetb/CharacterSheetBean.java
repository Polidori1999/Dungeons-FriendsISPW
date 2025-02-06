package it.uniroma2.marchidori.maininterface.bean.charactersheetb;


public class CharacterSheetBean {

    private CharacterInfoBean infoBean;
    private CharacterStatsBean statsBean;

    public CharacterSheetBean() {

        // costruttore vuoto
    }

    public CharacterSheetBean(CharacterInfoBean info, CharacterStatsBean abilityScores) {
        this.infoBean = info;
        this.statsBean = abilityScores;
    }

    // Getter / Setter
    public CharacterInfoBean getInfoBean() {
        return infoBean;
    }

    public void setInfoBean(CharacterInfoBean infoBean) {

        this.infoBean = infoBean;
    }

    public CharacterStatsBean getStatsBean() {
        return statsBean;
    }

    public void setStatsBean(CharacterStatsBean statsBean) {
        this.statsBean = statsBean;
    }

    @Override
    public String toString() {
        // Esempio di output, usando i dati dei due bean
        return "[CharacterSheetBean] " +
                (infoBean != null ? infoBean.getName() : "NoName") +
                " (Class: " + (infoBean != null ? infoBean.getClasse() : "?") +
                ", Level: " + (infoBean != null ? infoBean.getLevel() : "?") + ")";
    }
}
