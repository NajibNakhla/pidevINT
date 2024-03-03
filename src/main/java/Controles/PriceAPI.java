package Controles;

import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Services.PriceAPIServices.fetchPriceAndAvailability;
import static Services.PriceAPIServices.postJob;

public class PriceAPI {
    private String item;

    @FXML
    private TextField available;

    @FXML
    private ImageView image;

    @FXML
    private TextField nameT;

    @FXML
    private TextField price;

    @FXML
    private Label url;


    // Method to set the selected name
    public void setSelectedName(String selectedName) {
        this.item = selectedName;
        initialize(); // Reinitialize with the new item
    }

    @FXML
    public void initialize() {
        // Initialize with the selected item
        if (item != null) {
            nameT.setText(item);
            try {
                JsonObject jobResponse = postJob(item);
                String jobId = jobResponse.get("job_id").getAsString();
                System.out.println("Job ID: " + jobId);

                // Fetch price and availability
                String priceAndAvailability = fetchPriceAndAvailability(jobId);
                System.out.println(fetchPriceAndAvailability(jobId));
                // Parse priceAndAvailability string to extract information
                Pattern pattern = Pattern.compile("Name: (.+)\nURL: (.+)\nImage URL: (.+)\nPrice: (.+) EUR\nAvailability: (.+)");
                Matcher matcher = pattern.matcher(priceAndAvailability);
                if (matcher.find()) {
                    String name = matcher.group(1);
                    String imageURL = matcher.group(3);
                    String price = matcher.group(4);
                    String availability = matcher.group(5);


                    // Set the extracted information to UI elements
                    nameT.setText(name);
                    url.setText(matcher.group(2));
                    this.price.setText(price + " EUR");
                    available.setText(availability);

                    // Load image from URL and set it to ImageView
                    Image img = new Image(imageURL);
                    image.setImage(img);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

    @FXML
    void visitWebsiteButton(ActionEvent event) {
        String urlText = url.getText();
        System.out.println(urlText);

        if (urlText != null && !urlText.isEmpty()) {
            try {
                openInBrowser(urlText);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                // Handle the exception or inform the user
                System.err.println("Error opening the URL: " + e.getMessage());
            }
        } else {
            // URL is empty or null
            // Handle the case where the URL is not provided
            System.err.println("URL is empty or null.");
        }
    }

    private void openInBrowser(String url) throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URI(url));


    }





}

