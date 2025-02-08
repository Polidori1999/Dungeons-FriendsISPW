package it.uniroma2.marchidori.maininterface.repository;

import it.uniroma2.marchidori.maininterface.entity.RuleBook;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.uniroma2.marchidori.maininterface.factory.RuleBookFactory.createRuleBook;

public class RulesRepository {

    private RulesRepository() {}

    // Lista statica per contenere i RuleBook
    private static final List<RuleBook> ruleBookList = new ArrayList<>();

    // Recupera il percorso dalla variabile d'ambiente oppure usa come default la cartella "rulesbook" nella directory di lavoro
    private static final String FOLDERPATH = Optional.ofNullable(System.getenv("RULES_FOLDER_PATH"))
            .orElse(Paths.get(System.getProperty("user.dir"), "rulesbook").toAbsolutePath().toString());

    static {
        ruleBookList.add(createRuleBook("Regole di base",
                Paths.get(FOLDERPATH, "Regole di base.pdf").toAbsolutePath().toString(), true));
        ruleBookList.add(createRuleBook("Regole Avanzate",
                Paths.get(FOLDERPATH, "Regole Avanzate.pdf").toAbsolutePath().toString(), false));
    }

    public static List<RuleBook> getAllBooks() {
        // Ritorna una copia della lista per evitare modifiche dirette
        return new ArrayList<>(ruleBookList);
    }
}
