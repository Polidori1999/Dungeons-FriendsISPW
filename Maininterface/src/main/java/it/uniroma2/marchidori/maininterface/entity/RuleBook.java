package it.uniroma2.marchidori.maininterface.entity;

public class RuleBook {

    private String rulesBookName;
    private String path;
    private boolean isObtained;

    public RuleBook(String rulesBookName, String path, boolean obtained) {
        this.rulesBookName = rulesBookName;
        this.path = path;
        this.isObtained = obtained;
    }

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
