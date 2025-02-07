package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.entity.RuleBook;

public class RuleBookFactory {

    private RuleBookFactory() {}

    public static RuleBook createRuleBook(String name, String path, boolean bool) {
        return new RuleBook(name, path, bool);
    }
}
