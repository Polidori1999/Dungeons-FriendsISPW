package it.uniroma2.marchidori.maininterface.entity;

/**
 * Entity "CharacterSheet":
 * Aggrega i dati relativi a un personaggio,
 * suddivisi tra "CharacterInfo" e "CharacterStats".
 */
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

    // Getter e Setter per i due oggetti
    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }
    public void setCharacterInfo(CharacterInfo characterInfo) {
        this.characterInfo = characterInfo;
    }

    public CharacterStats getCharacterStats() {
        return characterStats;
    }
    public void setCharacterStats(CharacterStats characterStats) {
        this.characterStats = characterStats;
    }

    /**
     * Metodi delegati per comodità, così se il Controller fa
     * existing.setRace(...) funziona senza creare da zero
     * un CharacterInfo e un CharacterStats.
     */

    // ---------- INFO ----------
    public String getName() {
        return characterInfo.getName();
    }
    public void setName(String name) {
        characterInfo.setName(name);
    }

    public String getRace() {
        return characterInfo.getRace();
    }
    public void setRace(String race) {
        characterInfo.setRace(race);
    }

    public int getAge() {
        return characterInfo.getAge();
    }
    public void setAge(int age) {
        characterInfo.setAge(age);
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

    // ---------- STATS ----------
    public int getStrength() {
        return characterStats.getStrength();
    }
    public void setStrength(int strength) {
        characterStats.setStrength(strength);
    }

    public int getDexterity() {
        return characterStats.getDexterity();
    }
    public void setDexterity(int dexterity) {
        characterStats.setDexterity(dexterity);
    }

    public int getIntelligence() {
        return characterStats.getIntelligence();
    }
    public void setIntelligence(int intelligence) {
        characterStats.setIntelligence(intelligence);
    }

    public int getWisdom() {
        return characterStats.getWisdom();
    }
    public void setWisdom(int wisdom) {
        characterStats.setWisdom(wisdom);
    }

    public int getCharisma() {
        return characterStats.getCharisma();
    }
    public void setCharisma(int charisma) {
        characterStats.setCharisma(charisma);
    }

    public int getConstitution() {
        return characterStats.getConstitution();
    }
    public void setConstitution(int constitution) {
        characterStats.setConstitution(constitution);
    }
}
