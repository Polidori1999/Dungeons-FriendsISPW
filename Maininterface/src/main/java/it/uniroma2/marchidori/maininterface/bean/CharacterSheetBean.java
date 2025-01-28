package it.uniroma2.marchidori.maininterface.bean;


/**
 * Bean (DTO) per scambiare i dati di CharacterSheet
 * tra la Boundary (UI) e la Control.
 */
public class CharacterSheetBean {

    private String name;
    private String race;
    private String age;
    private String classe;
    private String level;
    private String strength;
    private String dexterity;
    private String intelligence;
    private String wisdom;
    private String charisma;
    private String constitution;

    public CharacterSheetBean() {
        // costruttore vuoto
    }

    public CharacterSheetBean(String name, String race, String age, String classe, String level,
                              String strength, String dexterity, String intelligence,
                              String wisdom, String charisma, String constitution) {
        this.name = name;
        this.race = race;
        this.age = age;
        this.classe = classe;
        this.level = level;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.constitution = constitution;
    }

    // Getter/Setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }
    public void setRace(String race) {
        this.race = race;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getClasse() {
        return classe;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    public String getStrength() {
        return strength;
    }
    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDexterity() {
        return dexterity;
    }
    public void setDexterity(String dexterity) {
        this.dexterity = dexterity;
    }

    public String getIntelligence() {
        return intelligence;
    }
    public void setIntelligence(String intelligence) {
        this.intelligence = intelligence;
    }

    public String getWisdom() {
        return wisdom;
    }
    public void setWisdom(String wisdom) {
        this.wisdom = wisdom;
    }

    public String getCharisma() {
        return charisma;
    }
    public void setCharisma(String charisma) {
        this.charisma = charisma;
    }

    public String getConstitution() {
        return constitution;
    }
    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    @Override
    public String toString() {
        return "[CharacterSheetBean] " + name + " (Class: " + classe + ", Level: " + level + ")";
    }
}
