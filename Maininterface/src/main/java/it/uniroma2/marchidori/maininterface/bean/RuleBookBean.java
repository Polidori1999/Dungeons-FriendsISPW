package it.uniroma2.marchidori.maininterface.bean;

public class RuleBookBean {
    //comment
    private String rulesBookName;
    private String path;
    private boolean isObtained;

    public RuleBookBean(String rulesBookName, String path, boolean isObtained) {
        this.rulesBookName = rulesBookName;
        this.path = path;
        this.isObtained = isObtained;
    }

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
