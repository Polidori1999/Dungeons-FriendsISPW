package it.uniroma2.marchidori.maininterface.repository;

import it.uniroma2.marchidori.maininterface.entity.RuleBook;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.factory.RuleBookFactory.createRuleBook;

public class RulesRepository {

    private static final Logger logger = Logger.getLogger(RulesRepository.class.getName());

    // Costruttore privato per evitare istanziazioni
    private RulesRepository() {}

    // Lista statica per contenere i RuleBook
    private static List<RuleBook> ruleBookList = new ArrayList<>();

    // Recupera il percorso dalla variabile d'ambiente oppure usa come default la cartella "it.uniroma2.marchidori.maininterface.rulesbook"
    private static final String FOLDERPATH = "src/main/java/it/uniroma2/marchidori/maininterface/rulesbook";

    private static String buildFilePath(String fileName) {
        Path path = Paths.get(FOLDERPATH, fileName);
        if (!Files.exists(path)) {
            throw new IllegalStateException("File non trovato: " + path.toAbsolutePath().toString());
        }
        return path.toAbsolutePath().toString();
    }

    // Blocco statico per inizializzare la lista dei RuleBook
    static {
        // Stampa di debug per verificare il percorso della cartella
        logger.info("FOLDERPATH: " + FOLDERPATH);

        // Costruzione e controllo dei percorsi per i file PDF
        String baseRulesPath = buildFilePath("Regole di base.pdf");
        String advancedRulesPath = buildFilePath("Regole Avanzate.pdf");

        logger.log(Level.INFO, "Path per Regole di base:{}",baseRulesPath);
        logger.log(Level.INFO, "Path per Regole Avanzate:{}", advancedRulesPath);

        // Creazione dei RuleBook tramite il factory method e aggiunta alla lista
        ruleBookList.add(createRuleBook("Regole di base", baseRulesPath, true));
        ruleBookList.add(createRuleBook("Regole Avanzate", advancedRulesPath, false));
    }

    /**
     * Restituisce una copia della lista dei RuleBook per evitare modifiche dirette.
     *
     * @return lista dei RuleBook
     */
    public static List<RuleBook> getAllBooks() {
        return new ArrayList<>(ruleBookList);
    }
    public static void setAllBooks(List<RuleBook> books) {
        ruleBookList = books;
    }
}
