package it.uniroma2.marchidori.maininterface.bean;

public class CharacterSheetBean {

    private CharacterInfoBean info;
    private AbilityScoresBean abilityScores;

    public CharacterSheetBean() {
        // costruttore vuoto
    }

    public CharacterSheetBean(CharacterInfoBean info, AbilityScoresBean abilityScores) {
        this.info = info;
        this.abilityScores = abilityScores;
    }

    // Getter / Setter
    public CharacterInfoBean getInfo() {
        return info;
    }

    public void setInfo(CharacterInfoBean info) {
        this.info = info;
    }

    public AbilityScoresBean getAbilityScores() {
        return abilityScores;
    }

    public void setAbilityScores(AbilityScoresBean abilityScores) {
        this.abilityScores = abilityScores;
    }

    @Override
    public String toString() {
        // Esempio di output, usando i dati dei due bean
        return "[CharacterSheetBean] " +
                (info != null ? info.getName() : "NoName") +
                " (Class: " + (info != null ? info.getClasse() : "?") +
                ", Level: " + (info != null ? info.getLevel() : "?") + ")";
    }
}
