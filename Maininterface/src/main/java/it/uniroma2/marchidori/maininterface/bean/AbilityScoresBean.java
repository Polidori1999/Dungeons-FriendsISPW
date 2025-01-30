package it.uniroma2.marchidori.maininterface.bean;



public class AbilityScoresBean {

    private String strength;
    private String dexterity;
    private String intelligence;
    private String wisdom;
    private String charisma;
    private String constitution;

    public AbilityScoresBean() {
        // costruttore vuoto
    }


    public AbilityScoresBean(String strength, String dexterity,
                             String intelligence, String wisdom,
                             String charisma, String constitution) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.constitution = constitution;
    }

    // Getter / Setter
    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }

    public String getDexterity() { return dexterity; }
    public void setDexterity(String dexterity) { this.dexterity = dexterity; }

    public String getIntelligence() { return intelligence; }
    public void setIntelligence(String intelligence) { this.intelligence = intelligence; }

    public String getWisdom() { return wisdom; }
    public void setWisdom(String wisdom) { this.wisdom = wisdom; }

    public String getCharisma() { return charisma; }
    public void setCharisma(String charisma) { this.charisma = charisma; }

    public String getConstitution() { return constitution; }
    public void setConstitution(String constitution) { this.constitution = constitution; }
}
