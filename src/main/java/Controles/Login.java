package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Login {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField emailField;

    @FXML
    private Text forgotPass;

    @FXML
    private Button login;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text register;

    @FXML
    void login(ActionEvent event) {
        UserService us = new UserService();
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all the required fields");
            alert.show();
        }
        if (!us.emailExists(emailField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This account doesn't exist !");
            alert.show();
            return;
        }
        us.login(emailField.getText(),passwordField.getText());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserProfile.fxml"));

        try {
            Parent root = loader.load();
            UserProfile pi = loader.getController();
            //pi.setId(us.getUserId(email.getText()));
           // pi.setLabel(user.getFirstName());
            emailField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void ForgotPass(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ForgotPass.fxml"));
        try {
            Parent root = loader.load();
            ForgotPass fp = loader.getController();

            emailField.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void Register(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Register.fxml"));
        try {
            Parent root = loader.load();
            Register reg = loader.getController();

            emailField.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void initialize() {


    }

}
