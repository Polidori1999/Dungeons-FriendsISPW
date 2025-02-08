package it.uniroma2.marchidori.maininterface.boundary.consultrules;


import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import java.io.IOException;

public class ConsultRulesGuestBoundary extends ConsultRulesBoundary {

    @Override
    public void initialize() {
        super.initialize();
        // Assicurati che consultButton e buyButton siano di tipo TableColumn<RuleBookBean, Button>
        TableColumnUtils.setupButtonColumn(consultButton, "Consult Now", ruleBook -> {
            try {
                changeScene(SceneNames.LOGIN);
            } catch (IOException e) {
                throw new SceneChangeException(e.getMessage());
            }
        });
        TableColumnUtils.setupButtonColumn(buyButton, "buy now!", ruleBook -> {
            try {
                changeScene(SceneNames.LOGIN);
            } catch (IOException e) {
                throw new SceneChangeException(e.getMessage());
            }
        });
    }
}
