package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.exception.PayPalPaymentException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

public class PayPalPaymentController {

    // Per la Sandbox
    private static final String PAYPAL_API_BASE = "https://api-m.sandbox.paypal.com";

    /**
     * Ottiene l'access token per autenticare le richieste all'API PayPal (OAuth2 client_credentials).
     */
    public String getAccessToken() throws IOException, InterruptedException, PayPalPaymentException {
        String clientId;
        String clientSecret;
        HttpClient httpClient = HttpClient.newHttpClient();

        FileInputStream fis = new FileInputStream("src/main/resources/pp_config.properties");
        Properties properties = new Properties();
        properties.load(fis);

        clientId = properties.getProperty("pp.user");
        clientSecret = properties.getProperty("pp.password");
        String auth = clientId + ":" + clientSecret;
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
            int index = body.indexOf("\"access_token\":\"");
            if (index == -1) {
                throw new PayPalPaymentException("access_token non trovato nel JSON!");
            }
            int start = index + "\"access_token\":\"".length();
            int end = body.indexOf("\"", start);
            return body.substring(start, end);
        } else {
            throw new PayPalPaymentException("Errore getAccessToken - HTTP "
                    + response.statusCode() + "\n" + response.body());
        }
    }

    /**
     * Crea un ordine di pagamento su PayPal (v2/checkout/orders) con importo e valuta specificati.
     * Restituisce la **risposta grezza** in formato JSON. In questa risposta,
     * ci saranno i link da cui estrarre l'URL "approve" (dove l'utente deve andare per pagare).
     */
    public String createOrder(String accessToken, String currency, String amount)
            throws IOException, InterruptedException, PayPalPaymentException {

        HttpClient httpClient = HttpClient.newHttpClient();

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
            return response.body();
        } else {
            throw new PayPalPaymentException("Errore createOrder - HTTP "
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
        String relApprove = "\"rel\":\"approve\"";
        int index = createOrderResponse.indexOf(relApprove);
        if (index == -1) {
            return null;
        }

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
