package it.uniroma2.marchidori.maininterface.boundary.consultrules;


import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import java.io.IOException;

public class ConsultRulesGuestBoundary extends ConsultRulesBoundary {

    @Override
    public void initialize() {
        super.initialize();
        initializeButton("Consult Now");
        initializeButton("buy now!");
    }

    private void initializeButton(String string){
        TableColumnUtils.setupButtonColumn(buyButton, string, ruleBook -> {
            try {
                changeScene(SceneNames.LOGIN);
            } catch (IOException e) {
                throw new SceneChangeException(e.getMessage());
            }
        });

    }




}
