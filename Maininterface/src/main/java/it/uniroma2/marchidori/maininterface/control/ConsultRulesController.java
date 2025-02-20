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
import java.net.URISyntaxException;
import java.util.List;

public class ConsultRulesController implements UserAwareInterface {
    private Jout jout = new Jout(this.getClass().getSimpleName());

    public ConsultRulesController() {
        //void
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

    //apertura file
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



    public void startPayPalPayment(double amount) {
        try {
            RulesRepository.getAllBooks().get(1).setObtained(true);
            PayPalPaymentController payCtrl = new PayPalPaymentController();

            //Ottieni access token
            String accessToken = payCtrl.getAccessToken();
            jout.print("AccessToken = " + accessToken);

            //Crea ordine
            String createOrderResponse = payCtrl.createOrder(accessToken, "EUR", String.valueOf(amount));
            jout.print("createOrderResponse = " + createOrderResponse);

            //Estrai link "approve"
            String approveLink = payCtrl.extractApproveLink(createOrderResponse);
            if (approveLink == null) {
                jout.print("Impossibile trovare il link di approvazione nel JSON di risposta PayPal");
                return;
            }

            // Apri il browser con il link PayPal
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new java.net.URI(approveLink));
            } else {
                jout.print("Apertura browser non supportata su questo sistema!");
            }

        } catch (InterruptedException e) {
            // Re-interrupt the thread if an InterruptedException is caught
            Thread.currentThread().interrupt();  // Preserve the interrupt status
        } catch (IOException | PayPalPaymentException | URISyntaxException e) {
            jout.print("help");
        }
    }

}
