package it.uniroma2.marchidori.maininterface.entity;


import java.awt.*;

public class CharacterSheet {
    public String name;
    public String level;
    public String race;
    public String age;
    public String classe;
    public Button editButton;

    public Button deleteButton;
    public String strength;
    public String dexterity;
    public String intelligence;
    public String wisdom;
    public String charisma;
    public String constitution;

    CharacterSheet (String name, String race, String age, String classe, String level, String strength, String dexterity, String intelligence, String wisdom, String charisma, String constitution) {
        this.name = name;
        this.race = race;
        this.age = age;
        this.classe = classe;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.constitution = constitution;
        this.level = level;
        this.editButton = new Button("Edit Character");
        this.deleteButton = new Button("Delete Character");
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

    public Button getEditButton() {
        return editButton;
    }
    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }
    public Button getDeleteButton() {
        return deleteButton;
    }
    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
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
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
}