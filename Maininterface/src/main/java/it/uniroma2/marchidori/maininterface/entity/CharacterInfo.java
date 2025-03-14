package it.uniroma2.marchidori.maininterface.entity;

public class CharacterInfo {

    private String name;
    private String race;
    private int age;
    private String classe;
    private int level;

    // Costruttore vuoto
    public CharacterInfo() {}


    // Costruttore con parametri "base"
    public CharacterInfo(String name, String race, int age, String classe, int level) {
        this.name = name;
        this.race = race;
        this.age = age;
        this.classe = classe;
        this.level = level;
    }

    // Getter & Setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRace() {
        return race;
    }
    public int getAge() {
        return age;
    }
    public String getClasse() {
        return classe;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
}