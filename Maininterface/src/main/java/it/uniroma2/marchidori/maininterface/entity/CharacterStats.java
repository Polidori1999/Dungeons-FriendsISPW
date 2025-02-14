package it.uniroma2.marchidori.maininterface.entity;

public class CharacterStats {

    private int strength;
    private int dexterity;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int constitution;


    // Costruttore vuoto (per riflessione / framework)
    public CharacterStats() {
    }

    // Costruttore con parametri
    public CharacterStats(int strength, int dexterity, int intelligence,
                          int wisdom, int charisma, int constitution) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.constitution = constitution;
    }

    // Getter e Setter
    public int getStrength() {
        return strength;
    }
    public int getDexterity() {
        return dexterity;
    }
    public int getIntelligence() {
        return intelligence;
    }
    public int getWisdom() {
        return wisdom;
    }
    public int getCharisma() {
        return charisma;
    }
    public int getConstitution() {
        return constitution;
    }

}
