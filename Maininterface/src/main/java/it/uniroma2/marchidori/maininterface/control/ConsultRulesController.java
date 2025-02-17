package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.bean.RuleBookBean;
import it.uniroma2.marchidori.maininterface.entity.RuleBook;
import it.uniroma2.marchidori.maininterface.repository.RulesRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import it.uniroma2.marchidori.maininterface.exception.PayPalPaymentException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConsultRulesController implements UserAwareInterface {
    private Jout jout = new Jout(this.getClass().getSimpleName());

    public ConsultRulesController() {
        // Empty constructor
    }

    @Override
    public void setCurrentUser(UserBean user) {
        // Non serve per questo controller
    }

    public ObservableList<RuleBookBean> getAllRuleBooks() {
        List<RuleBook> ruleBooks = RulesRepository.getAllBooks();
        List<RuleBookBean> ruleBookBeans = ruleBooks.stream()
                .map(rb -> new RuleBookBean(rb.getRulesBookName(), rb.getPath(), rb.isObtained()))
                .toList();
        return FXCollections.observableArrayList(ruleBookBeans);
    }

    /**
     * Prova ad aprire il file associato al percorso indicato, se esiste.
     */
    public void openFileIfExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            jout.print("Errore: Il percorso del file è vuoto o nullo.");
            return;
        }
        String normalizedPath = filePath.replace("/", File.separator).replace("\\", File.separator);
        File file = new File(normalizedPath);
        if (!file.exists()) {
            jout.print("Errore: Il file non esiste! Path: " + normalizedPath);
            return;
        }
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
                jout.print("File aperto con successo: " + normalizedPath);
            } else {
                jout.print("Il sistema non supporta l'apertura dei file.");
            }
        } catch (IOException e) {
            jout.print(String.format("Errore durante l'apertura del file: %s", normalizedPath));
        }
    }

    /**
     * Aggiorna lo stato del RuleBook nel repository.
     * Cerca il RuleBook con lo stesso nome e imposta il flag obtained in base al bean aggiornato.
     * Infine persiste le modifiche.
     *
     * @param updatedBean Il RuleBookBean aggiornato.
     */
    public void updateRuleBook(RuleBookBean updatedBean) {
        // Ottieni la lista corrente dei RuleBook dal repository
        List<RuleBook> ruleBooks = RulesRepository.getAllBooks();
        // Cerca il RuleBook da aggiornare (corrispondenza per nome)
        for (RuleBook rb : ruleBooks) {
            if (rb.getRulesBookName().equals(updatedBean.getRulesBookName())) {
                rb.setObtained(true);
                break;
            }
        }
        // Persiste le modifiche: si assume che il repository offra questo metodo.
        RulesRepository.setAllBooks(ruleBooks);
    }


    //VEDERE SE SPOSTARE IN CONTROLLER
    public void startPayPalPayment(double amount) throws PayPalPaymentException {
        try {
            RulesRepository.getAllBooks().get(1).setObtained(true);
            PayPalPaymentController payCtrl = new PayPalPaymentController();

            // 1) Ottieni access token
            String accessToken = payCtrl.getAccessToken();
            jout.print("AccessToken = " + accessToken);

            // 2) Crea ordine (EUR e importo a piacere)
            String createOrderResponse = payCtrl.createOrder(accessToken, "EUR", String.valueOf(amount));
            jout.print("createOrderResponse = " + createOrderResponse);

            // 3) Estrai link "approve"
            String approveLink = payCtrl.extractApproveLink(createOrderResponse);
            if (approveLink == null) {
                jout.print( "Impossibile trovare il link di approvazione nel JSON di risposta PayPal");
                return;
            }

            // 4) Apri il browser con il link PayPal
            // In un'app desktop JavaFX, puoi fare:
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new java.net.URI(approveLink));
            } else {
                jout.print("Apertura browser non supportata su questo sistema!");
            }

            // A questo punto l'utente vede la pagina PayPal, effettua il login e completa il pagamento.
            // Se desideri catturare in un secondo momento, dovrai salvare l'orderID e
            // chiamare "capture" su /v2/checkout/orders/{orderID}/capture.

        } catch (IOException | InterruptedException e) {
            jout.print("Errore durante la comunicazione con PayPal: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new PayPalPaymentException("Errore durante il pagamento PayPal.", e);  // Usa l'eccezione personalizzata
        } catch (Exception e) {
            jout.print("Errore durante il pagamento PayPal: ");
            throw new PayPalPaymentException("Errore sconosciuto durante il pagamento PayPal.", e);  // Gestisci altre eccezioni
        }
    }
}
