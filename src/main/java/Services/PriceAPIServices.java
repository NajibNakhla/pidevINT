package Services;


import com.google.gson.*;
import okhttp3.*;

import java.io.IOException;

public class PriceAPIServices {
    private static final String API_TOKEN = "DOJYDUKUHWOLZMRRTYAINSIYHYOUCGZYSYIQKBSTXDWPQPTKWKLQZFBJPXDKTTKZ";
    public static JsonObject postJob(String term) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = "{\"source\":\"amazon\",\"country\":\"fr\",\"topic\":\"product_and_offers\",\"key\":\"term\",\"values\":\"" + term + "\",\"max_age\":\"1440\",\"timeout\":\"5\",\"token\":\"" + API_TOKEN + "\",\"webhook_method\":\"POST\",\"webhook_download_format\":\"json\",\"webhook_url\":\"https://app.priceapi.com/v2/jobs\"}";
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("https://api.priceapi.com/v2/jobs?token=" + API_TOKEN)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            System.out.println(responseBody);
            return new Gson().fromJson(responseBody, JsonObject.class);
        }
    }
    public static String fetchPriceAndAvailability(String jobId) throws IOException {
        try {
            Thread.sleep(5000); // Wait for 5 seconds
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.priceapi.com/v2/jobs/" + jobId + "/download?token=" + API_TOKEN)
                .get()
                .addHeader("accept", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String jsonResponse = response.body().string();
            String result = "";

            System.out.println(jsonResponse);

            JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
            JsonArray resultsArray = jsonObject.getAsJsonArray("results");

            for (JsonElement resultElement : resultsArray) {
                JsonObject resultObject = resultElement.getAsJsonObject();
                JsonObject contentObject = resultObject.getAsJsonObject("content");
                String name = contentObject.get("name").getAsString();
                String url = contentObject.get("url").getAsString();
                String image_url = contentObject.get("image_url").getAsString();

                JsonObject buyboxObject = contentObject.getAsJsonObject("buybox");
                String availabilityText = buyboxObject.get("availability_text").getAsString();

                JsonArray offersArray = contentObject.getAsJsonArray("offers");
                for (JsonElement offerElement : offersArray) {
                    JsonObject offerObject = offerElement.getAsJsonObject();
                    String price = offerObject.get("price").getAsString();
                    String currency = offerObject.get("currency").getAsString();

                    result = "Name: " + name+"\n"+"URL: " + url+"\n"+"Image URL: " + image_url+"\n"+"Price: " +price+" "+currency+"\n"+"Availability: "+availabilityText+"\n";

                }
            }
            return result;

        }



}}
