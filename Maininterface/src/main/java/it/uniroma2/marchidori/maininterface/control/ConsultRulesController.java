package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.entity.RuleBook;
import it.uniroma2.marchidori.maininterface.repository.RulesRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ConsultRulesController implements UserAwareInterface {
    private UserBean currentUser;
    private User currentEntity = Session.getCurrentUser();

    public ConsultRulesController() {
        // Empty constructor
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public ObservableList<RuleBookBean> getAllRuleBooks() {
        List<RuleBook> ruleBooks = RulesRepository.getAllBooks();

        // Sostituito Collectors.toList() con toList()
        List<RuleBookBean> ruleBookBeans = ruleBooks.stream()
                .map(rb -> new RuleBookBean(rb.getRulesBookName(), rb.getPath(), rb.isObtained()))
                .toList(); // <-- Metodo più efficiente e consigliato da SonarQube

        return FXCollections.observableArrayList(ruleBookBeans);
    }
}
