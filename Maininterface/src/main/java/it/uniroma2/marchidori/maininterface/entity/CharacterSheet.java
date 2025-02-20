package it.uniroma2.marchidori.maininterface.entity;

public class CharacterSheet {

    private CharacterInfo characterInfo;
    private CharacterStats characterStats;

    // Costruttore vuoto
    public CharacterSheet() {
    }

    // Costruttore principale a 2 parametri
    public CharacterSheet(CharacterInfo characterInfo, CharacterStats characterStats) {
        this.characterInfo = characterInfo;
        this.characterStats = characterStats;
    }

    // Getter & Setter per i due oggetti
    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }

    public CharacterStats getCharacterStats() {
        return characterStats;
    }

    public String getName() {
        return characterInfo.getName();
    }
    public void setName(String name) {
        characterInfo.setName(name);
    }

    public String getRace() {
        return characterInfo.getRace();
    }

    public String getClasse() {
        return characterInfo.getClasse();
    }
    public void setClasse(String classe) {
        characterInfo.setClasse(classe);
    }

    public int getLevel() {
        return characterInfo.getLevel();
    }
    public void setLevel(int level) {
        characterInfo.setLevel(level);
    }

    @Override
    public String toString() {
        return "CharacterSheet [name=" + getName() + ", classe=" + getClasse() + ", level=" + getLevel() + "]";
    }
}
