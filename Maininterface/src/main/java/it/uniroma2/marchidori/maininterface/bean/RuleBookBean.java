package it.uniroma2.marchidori.maininterface.bean;

public class RuleBookBean {
    //comment
    private String rulesBookName;

    public RuleBookBean(String rulesBookName) {
        this.rulesBookName = rulesBookName;
    }

    public String getRulesBookName() {

        return rulesBookName;
    }
    public void setRulesBookName(String rulesBookName) {
        this.rulesBookName = rulesBookName;
    }
}
