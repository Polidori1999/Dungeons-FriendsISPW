package it.uniroma2.marchidori.maininterface.entity;

public class CharacterInfo {

    private String name;
    private String race;
    private int age;
    private String classe;
    private int level;

    // Costruttore vuoto (utile se serve a framework o riflessione)
    public CharacterInfo() {
    }


    // Costruttore con parametri
    public CharacterInfo(String name, String race, int age, String classe, int level) {
        this.name = name;
        this.race = race;
        this.age = age;
        this.classe = classe;
        this.level = level;
    }

    // Getter e Setter
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

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
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
