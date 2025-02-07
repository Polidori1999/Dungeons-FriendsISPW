package it.uniroma2.marchidori.maininterface.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    /**
     * Configura una colonna in modo dinamico, aggiornando il testo e lo stato del pulsante
     * in base al bean della riga.
     *
     * @param <T>              il tipo di elemento contenuto nella tabella
     * @param column           la colonna da configurare
     * @param textFunction     funzione che, dato il bean, restituisce il testo del pulsante
     * @param disablePredicate predicato che, dato il bean, indica se il pulsante deve essere disabilitato
     * @param action           l'azione da eseguire al click sul pulsante, ricevendo il bean della riga
     */
    public static <T> void setupDynamicButtonColumn(TableColumn<T, Button> column,
                                                    Function<T, String> textFunction,
                                                    Predicate<T> disablePredicate,
                                                    Consumer<T> action) {
        column.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(new Button()));
        column.setCellFactory(col -> new TableCell<T, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                // Controlla se la riga è vuota o se l'indice è fuori dai limiti
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    T rowItem = getTableView().getItems().get(getIndex());
                    if (item == null) {
                        item = new Button();
                    }
                    item.setText(textFunction.apply(rowItem));
                    item.setDisable(disablePredicate.test(rowItem));
                    item.setOnAction(e -> action.accept(rowItem));
                    setGraphic(item);
                }
            }
        });
    }
}
