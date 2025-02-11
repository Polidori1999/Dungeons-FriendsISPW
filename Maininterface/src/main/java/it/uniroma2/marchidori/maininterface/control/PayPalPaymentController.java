package it.uniroma2.marchidori.maininterface.control;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PayPalPaymentController {

    // Per la Sandbox
    private static final String PAYPAL_API_BASE = "https://api-m.sandbox.paypal.com";

    // Inserisci le TUE credenziali (client id e secret) create su PayPal Developer
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";

    /**
     * Ottiene l'access token per autenticare le richieste all'API PayPal (OAuth2 client_credentials).
     */
    public String getAccessToken() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        // Basic Auth: clientId + ":" + clientSecret in Base64
        String auth = CLIENT_ID + ":" + CLIENT_SECRET;
        String base64Auth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PAYPAL_API_BASE + "/v1/oauth2/token"))
                .header("Authorization", "Basic " + base64Auth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            String body = response.body();
            // Estrazione manuale del token (in un progetto reale conviene usare una libreria JSON)
            int index = body.indexOf("\"access_token\":\"");
            if (index == -1) {
                throw new RuntimeException("access_token non trovato nel JSON!");
            }
            int start = index + "\"access_token\":\"".length();
            int end = body.indexOf("\"", start);
            return body.substring(start, end);
        } else {
            throw new RuntimeException("Errore getAccessToken - HTTP "
                    + response.statusCode() + "\n" + response.body());
        }
    }

    /**
     * Crea un ordine di pagamento su PayPal (v2/checkout/orders) con importo e valuta specificati.
     * Restituisce la **risposta grezza** in formato JSON. In questa risposta,
     * ci saranno i link da cui estrarre l'URL "approve" (dove l'utente deve andare per pagare).
     */
    public String createOrder(String accessToken, String currency, String amount)
            throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();

        // Corpo JSON molto semplificato per la creazione dell'ordine
        // "intent": "CAPTURE" -> per catturare direttamente l'importo al momento del pagamento
        String jsonBody = "{\n"
                + "  \"intent\": \"CAPTURE\",\n"
                + "  \"purchase_units\": [\n"
                + "    {\n"
                + "      \"amount\": {\n"
                + "        \"currency_code\": \"" + currency + "\",\n"
                + "        \"value\": \"" + amount + "\"\n"
                + "      }\n"
                + "    }\n"
                + "  ]\n"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PAYPAL_API_BASE + "/v2/checkout/orders"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return response.body(); // JSON di risposta
        } else {
            throw new RuntimeException("Errore createOrder - HTTP "
                    + response.statusCode() + "\n" + response.body());
        }
    }

    /**
     * Esempio di metodo per estrarre l'URL di approvazione dal JSON di risposta
     * rest. Ritorna `null` se non trovato.
     *
     * Nel JSON troverai un array "links" contenente oggetti con "rel":"approve".
     * Qualcosa tipo:
     *  "links": [
     *    { "href": "...", "rel": "approve", "method": "GET" }
     *  ]
     */
    public String extractApproveLink(String createOrderResponse) {
        // Ricerca primitiva nel JSON
        // Link come:
        // "rel":"approve","method":"GET","href":"https://www.sandbox.paypal.com/checkoutnow?token=..."
        String relApprove = "\"rel\":\"approve\"";
        int index = createOrderResponse.indexOf(relApprove);
        if (index == -1) {
            return null;
        }
        // cerchiamo "href":"...":
        int hrefIndex = createOrderResponse.lastIndexOf("\"href\":\"", index);
        if (hrefIndex == -1) {
            return null;
        }
        int start = hrefIndex + "\"href\":\"".length();
        int end = createOrderResponse.indexOf("\"", start);
        if (end == -1) {
            return null;
        }
        return createOrderResponse.substring(start, end);
    }
}
