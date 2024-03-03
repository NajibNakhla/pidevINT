package Services;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import Entities.OpenExchangeRatesConnection;

import java.util.Arrays;
import java.util.List;

public class CurrencyService {

    private final OpenExchangeRatesConnection connection;

    public CurrencyService() {
        this.connection = new OpenExchangeRatesConnection();
    }


    public double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            // Update the API URL to include the base currency
            String apiUrl = "https://open.er-api.com/v6/latest/" + fromCurrency;

            // Set the new API URL in the connection
            connection.setApiUrl(apiUrl);

            // Get the exchange rate data for the specified base currency
            String response = connection.getExchangeRateData(apiUrl);

            if (response != null) {
                // Parse the JSON response
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

                // Extract the exchange rate for the specified target currency
                double exchangeRate = jsonResponse.getAsJsonObject("rates").get(toCurrency).getAsDouble();

                return exchangeRate;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0; // Return a default value or handle the error appropriately
    }




    public static List<String> getPredefinedCurrencyCodes() {
        // Add or replace with your desired list of currency codes
        return Arrays.asList("USD", "EUR", "TND");
    }





}


