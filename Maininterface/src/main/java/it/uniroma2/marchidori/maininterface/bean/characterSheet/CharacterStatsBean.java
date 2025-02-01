package it.uniroma2.marchidori.maininterface.bean.characterSheet;


public class CharacterStatsBean {

    private int strength;
    private int dexterity;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int constitution;

    public CharacterStatsBean() {
        // costruttore vuoto
    }


    public CharacterStatsBean(int strength, int dexterity,
                              int intelligence, int wisdom,
                              int charisma, int constitution) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.constitution = constitution;
    }

    // Getter / Setter
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }

    public int getDexterity() { return dexterity; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }

    public int getIntelligence() { return intelligence; }
    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }

    public int getWisdom() { return wisdom; }
    public void setWisdom(int wisdom) { this.wisdom = wisdom; }

    public int getCharisma() { return charisma; }
    public void setCharisma(int charisma) { this.charisma = charisma; }

    public int getConstitution() { return constitution; }
    public void setConstitution(int constitution) { this.constitution = constitution; }
}
