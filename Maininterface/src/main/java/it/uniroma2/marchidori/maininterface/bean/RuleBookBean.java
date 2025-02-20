package it.uniroma2.marchidori.maininterface.bean;

public class RuleBookBean {

    private String rulesBookName; //nome
    private String path; //percorso
    private boolean isObtained;


    //costruttore bean
    public RuleBookBean(String rulesBookName, String path, boolean isObtained) {
        this.rulesBookName = rulesBookName;
        this.path = path;
        this.isObtained = isObtained;
    }
    // Getter & Setter
    public String getRulesBookName() {

        return rulesBookName;
    }
    public String getPath() {
        return path;
    }
    public boolean isObtained() {
        return isObtained;
    }
    public void setObtained(boolean obtained) {
        isObtained = obtained;
    }
}
