package it.uniroma2.marchidori.maininterface;


public class CharacterSheet {
    public String name;
    public String race;
    public String age;
    public String classe;

    CharacterSheet (String name, String race, String age, String classe) {
        this.name = name;
        System.out.println(name);
        this.race = race;
        System.out.println(race);
        this.age = age;
        System.out.println(age);
        this.classe = classe;
        System.out.println(classe);
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

}
