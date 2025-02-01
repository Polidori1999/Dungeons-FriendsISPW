package it.uniroma2.marchidori.maininterface.bean.charactersheetb;

public class CharacterInfoBean {

    private String name;
    private String race;
    private int age;
    private String classe;
    private int level;


    public CharacterInfoBean() {
        // costruttore vuoto
    }

    public CharacterInfoBean(String name, String race, int age, String classe, int level) {
        this.name = name;
        this.race = race;
        this.age = age;
        this.classe = classe;
        this.level = level;
    }

    // Getter / Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}
