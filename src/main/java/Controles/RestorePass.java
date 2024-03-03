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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RestorePass {
    UserService userService= new UserService();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Label emailField;
    @FXML
    private TextField resetCodeTextField;
    private String email;

    public void setEmail(String email) {
        this.email = email;
        emailField.setId(email);
    }

    @FXML
    void handleRestorePassword(ActionEvent event) {

        if (newPasswordField.getText().equals(confirmPasswordField.getText())){
            userService.updatePassword(newPasswordField.getText(), emailField.getText());
            Alert alertE = new Alert(Alert.AlertType.INFORMATION);
            alertE.setTitle("Password updated");
            alertE.setHeaderText("Password updated");
            alertE.setContentText("Password updated !");
            alertE.show();
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
        }else {
            Alert alertE = new Alert(Alert.AlertType.ERROR);
            alertE.setTitle("Password mismatch");
            alertE.setHeaderText("Password mismatch");
            alertE.setContentText("The password field doesn't match the confirm password field !");
            alertE.showAndWait();
        }
    }


    @FXML
    void initialize() {
        assert confirmPasswordField != null : "fx:id=\"confirmPasswordField\" was not injected: check your FXML file 'RestorePass.fxml'.";
        assert newPasswordField != null : "fx:id=\"newPasswordField\" was not injected: check your FXML file 'RestorePass.fxml'.";
        assert resetCodeTextField != null : "fx:id=\"resetCodeTextField\" was not injected: check your FXML file 'RestorePass.fxml'.";

    }

}
