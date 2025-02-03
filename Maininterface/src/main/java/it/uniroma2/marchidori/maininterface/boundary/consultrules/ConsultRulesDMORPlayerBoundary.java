package it.uniroma2.marchidori.maininterface.boundary.consultrules;

import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.io.IOException;

public class ConsultRulesDMORPlayerBoundary extends ConsultRulesBoundary {

    @Override
    public void initialize() {
        super.initialize();
        // Utilizza le factory separate per ciascun bottone, passando l'etichetta e l'azione corrispondente
        consultButton.setCellFactory(col -> createActionCell("Consult Now", this::consultAction));
        buyButton.setCellFactory(col -> createActionCell("buy now!", this::buyAction));
    }

    /**
     * Crea una TableCell che contiene un bottone con l'etichetta specificata e l'azione fornita.
     *
     * @param label  l'etichetta da mostrare sul bottone
     * @param action la funzione da eseguire al click del bottone
     * @return una TableCell configurata
     */
    private TableCell<RuleBookBean, Void> createActionCell(String label, Runnable action) {
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
                            // Esegue l'azione passata
                            action.run();
                        });
                    }
                    setGraphic(actionBtn);
                }
            }
        };
    }

    /**
     * Metodo per gestire l'azione del bottone "Consult Now".
     * Implementa qui la logica desiderata.
     */
    private void consultAction() {
        try {
            // Lascia vuota l'implementazione o aggiungi la logica necessaria
            changeScene(SceneNames.LOGIN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo per gestire l'azione del bottone "buy now!".
     * Implementa qui la logica desiderata.
     */
    private void buyAction() {
        try {
            // Lascia vuota l'implementazione o aggiungi la logica necessaria
            changeScene(SceneNames.LOGIN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}