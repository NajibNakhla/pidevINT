package Entities;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OpenExchangeRatesConnection {

    private static String API_URL = "https://open.er-api.com/v6/latest/USD";
    private static final String API_KEY = "a5ebce9159e44a29bd13b12497391d8b";



    public double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            String apiUrl = "https://open.er-api.com/v6/latest/" + fromCurrency + "?apikey=" + API_KEY;
            String response = getExchangeRateData(apiUrl);

            if (response != null) {
                // Parse the JSON response
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

                // Extract the exchange rate for the specified currencies
                double exchangeRate = jsonResponse.getAsJsonObject("rates").get(toCurrency).getAsDouble();

                return exchangeRate;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0; // Return a default value or handle the error appropriately
    }

    public String getExchangeRateData(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";

            connection.disconnect();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setApiUrl(String apiUrl) {
        this.API_URL = apiUrl;
    }


}
