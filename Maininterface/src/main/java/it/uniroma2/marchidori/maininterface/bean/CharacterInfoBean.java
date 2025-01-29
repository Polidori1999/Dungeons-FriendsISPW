package it.uniroma2.marchidori.maininterface.bean;

public class CharacterInfoBean {

    private String name;
    private String race;
    private String age;
    private String classe;
    private String level;

    public CharacterInfoBean() {
        // costruttore vuoto
    }

    public CharacterInfoBean(String name, String race, String age, String classe, String level) {
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

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
