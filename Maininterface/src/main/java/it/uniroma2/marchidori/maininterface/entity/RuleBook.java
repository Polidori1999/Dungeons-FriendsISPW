package it.uniroma2.marchidori.maininterface.entity;

public class RuleBook {

    private String rulesBookName; //nome
    private String path;          //percorso
    private boolean isObtained;

    //costruttore con parametri "base"
    public RuleBook(String rulesBookName, String path, boolean obtained) {
        this.rulesBookName = rulesBookName;
        this.path = path;
        this.isObtained = obtained;
    }

    //Getter & Setter
    public String getRulesBookName() {
        return rulesBookName;
    }
    public void setRulesBookName(String rulesBookName) {
        this.rulesBookName = rulesBookName;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public boolean isObtained() {
        return isObtained;
    }
    public void setObtained(boolean obtained) {
        isObtained = obtained;
    }
}