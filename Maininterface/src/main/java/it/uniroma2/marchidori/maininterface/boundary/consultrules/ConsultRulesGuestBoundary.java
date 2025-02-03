package it.uniroma2.marchidori.maininterface.boundary.consultrules;

import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.io.IOException;

public class ConsultRulesGuestBoundary extends ConsultRulesBoundary {

    @Override
    public void initialize() {
        super.initialize();
        // Utilizza la factory comune per entrambe le colonne
        consultButton.setCellFactory(col -> createActionCell("Consult Now"));
        buyButton.setCellFactory(col -> createActionCell("buy now!"));
    }

    /**
     * Crea una TableCell che contiene un bottone con l'etichetta specificata.
     * Al click, viene eseguita l'azione di cambio scena verso LOGIN.
     *
     * @param label l'etichetta da mostrare sul bottone
     * @return una TableCell configurata
     */
    private TableCell<RuleBookBean, Void> createActionCell(String label) {
        return new TableCell<>() {
            private Button actionBtn;

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if (actionBtn == null) {
                        actionBtn = new Button(label);
                        actionBtn.setOnAction(event -> {
                            try {
                                changeScene(SceneNames.LOGIN);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    setGraphic(actionBtn);
                }
            }
        };
    }
}