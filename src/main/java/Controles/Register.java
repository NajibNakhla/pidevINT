package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import Entities.User;
import Services.UserService;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.oauth2.model.Userinfo;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Tools.OAuth2CallbackHandler;
import Tools.PassValidator;
import com.google.api.services.oauth2.Oauth2;

public class Register {
  //  private static final String APPLICATION_NAME = "Pennywise";
    public static final String APPLICATION_ID = "sq0idp-MyDU6VkX-TEbDAQHZ6DdcA";
    public static final String APPLICATION_SECRET = "sq0csp-1WkwrY7Jnq-rRCi9Grt8hFW2oP4kGkVpT3Bi6pSGne8";
    public static final String CLIENT_ID = "337942056144-qs4554hsk9hs0s0b09jcotc2asa8dqm9.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "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";
    public static final String CALLBACK_URL = "http://localhost:8080";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    //private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private final  String[] scopes = {
        "openid", // OpenID Connect authentication
                "email", // Access to user's email address
                "profile", // Access to user's basic profile information
        //"https://www.googleapis.com/auth/calendar", // Access to user's Google Calendar data
    };
    private GoogleAuthorizationCodeFlow flow;
    private HostServices hostServices;
  //  private static final String USER_AGENT = "Google-PeopleAPI-Java-Sample";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private PasswordField password;

    @FXML
    private Button registerButton;
    private Stage stage;
    private OAuth2CallbackHandler oauth2CallbackHandler;

    @FXML
    void register(ActionEvent event) {
        User user = new User(firstName.getText(), lastName.getText(), email.getText(), password.getText());
        UserService us = new UserService();
       // String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (firstName.getText().isEmpty() || password.getText().isEmpty()||lastName.getText().isEmpty()||email.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all the required fields");
            alert.show();
        }

        PassValidator passValidator = new PassValidator();
        if (!passValidator.isStrongPassword(password.getText())) {
            return;
        }

       // if(email.getText()!=)
        if(!isValidEmail(email.getText())){
            Alert alertE = new Alert(Alert.AlertType.INFORMATION);
            alertE.setTitle("Bad Email");
            alertE.setHeaderText("Bad email form");
            alertE.setContentText("Email must match email@email.email");
            alertE.showAndWait();

        }
        if (us.emailExists(email.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This account exists already !");
            alert.show();
            return;
        }
        try {
            us.register(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sign Up Successful");

            alert.show();
            System.out.println(us.getUserId(email.getText()));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Form1.fxml"));
            Parent root = loader.load();
            Form1 pi = loader.getController();
            pi.setId(us.getUserId(email.getText()));
            pi.setLabel(user.getFirstName());
            lastName.getScene().setRoot(root);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error adding user : " + e.getMessage());
            alert.show();
        }

    }
    @FXML
    void googleSignUp(ActionEvent event) {
        try {
            GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl().setRedirectUri(CALLBACK_URL);
            String authorizeUrl = url.build();
            hostServices.showDocument(authorizeUrl);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void handleOAuth2Callback(String authorizationCode) {
        try {
            GoogleTokenResponse tokenRes = exchangeCodeForAccessToken(authorizationCode);
            String email = getEmailAdress(tokenRes.getAccessToken());
            System.out.println("User email : "+ email);
            stage.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Close the authentication window and return to your application

    }
    private String getEmailAdress(String accessToken){
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
            Oauth2 oauth2= new Oauth2.Builder(HTTP_TRANSPORT,JSON_FACTORY,credential).build();
            Userinfo userinfo = oauth2.userinfo().get().execute();
            return userinfo.getEmail();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public GoogleTokenResponse exchangeCodeForAccessToken(String authorizationCode)throws Exception{
        GoogleAuthorizationCodeTokenRequest tokenReq = flow.newTokenRequest(authorizationCode);
        tokenReq.setRedirectUri(CALLBACK_URL);
        return tokenReq.execute();
    }


    @FXML
    void login(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        try {
            Parent root = loader.load();
            Login login = loader.getController();

            firstName.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private boolean isValidEmail(String email) {
        // Basic email validation using regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void initialize() {
        flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                JSON_FACTORY,
                CLIENT_ID,
                CLIENT_SECRET,
                Arrays.asList(scopes))
                .setAccessType("offline")
                .build();
        oauth2CallbackHandler = new OAuth2CallbackHandler();
        try {
            oauth2CallbackHandler.startServer();
            System.out.println("OAuth2 callback server started successfully.");
        } catch (IOException e) {
            System.err.println("Error starting OAuth2 callback server: " + e.getMessage());
        }

    }


}
