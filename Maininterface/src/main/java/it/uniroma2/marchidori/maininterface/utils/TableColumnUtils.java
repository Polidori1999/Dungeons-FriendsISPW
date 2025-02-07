package it.uniroma2.marchidori.maininterface.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import java.util.function.Consumer;

public final class TableColumnUtils {

    private TableColumnUtils() {
        // Evita l'istanza della classe di utilità
    }

    /**
     * Configura la colonna in modo che mostri un pulsante con il testo specificato
     * e, al click, esegua l'azione passata sul dato della riga corrente.
     *
     * @param <T>         il tipo di elemento contenuto nella tabella
     * @param column      la colonna da configurare
     * @param buttonText  il testo da mostrare sul pulsante
     * @param action      l'azione da eseguire, ricevendo il dato della riga
     */
    public static <T> void setupButtonColumn(TableColumn<T, Button> column,
                                             String buttonText,
                                             Consumer<T> action) {
        // Imposta il pulsante da mostrare in ogni cella
        column.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(new Button(buttonText)));

        // Imposta il comportamento della cella
        column.setCellFactory(col -> new TableCell<T, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    // Associa l'azione al pulsante; recupera l'elemento della riga corrente
                    item.setOnAction(e -> {
                        T rowItem = getTableView().getItems().get(getIndex());
                        action.accept(rowItem);
                    });
                }
            }
        });
    }
}
