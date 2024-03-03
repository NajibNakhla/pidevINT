package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPass {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField emailField;
    UserService userService= new UserService();

    @FXML
    void sendEmail(ActionEvent event) {
        String email = emailField.getText();
        String resetCode=generateResetCode();
        send(email,resetCode);
        userService.updateCode(email,resetCode);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Reset Code Sent");
        alert.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ResetCode.fxml"));

        try {
            Parent root = loader.load();
            ResetCode pi = loader.getController();
            pi.setEmail(emailField.getText());
            emailField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }
    void send(String email, String resetCode){
        String host ="smtp.gmail.com";
        String from = "eyaboukh@gmail.com";
        String pwd = "rslw ulsu seae mbib";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,pwd);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            message.setSubject("Password Reset Code");
            message.setText("Your password reset code is: " + resetCode);
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

    }

    private String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate 6-digit code
        return String.valueOf(code);
    }

    @FXML
    void initialize() {
        assert emailField != null : "fx:id=\"emailField\" was not injected: check your FXML file 'ForgotPass.fxml'.";

    }

}
