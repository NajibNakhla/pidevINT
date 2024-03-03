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
import javafx.scene.control.TextField;

public class ResetCode {
    UserService userService= new UserService();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField resetCodeTextField;
    @FXML
    private Label emailField;
    private String email;

    public void setEmail(String email) {
        this.email = email;
        emailField.setId(email);
    }

    @FXML
    void sendCode(ActionEvent event) {
        String resetCode = resetCodeTextField.getText();
        if(resetCodeTextField.getText().isEmpty()){
            Alert alertE = new Alert(Alert.AlertType.ERROR);
            alertE.setTitle("Select Code");
            alertE.setHeaderText("Select Code");
            alertE.setContentText("Field is empty, try again !");
            alertE.showAndWait();
        }
        if (userService.isValidResetCode(resetCode)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RestorePass.fxml"));
            try {
                Parent root = loader.load();
                RestorePass pi = loader.getController();
                pi.setEmail(emailField.getText());
                resetCodeTextField.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            Alert alertE = new Alert(Alert.AlertType.ERROR);
            alertE.setTitle("Wrong Code");
            alertE.setHeaderText("Wrong Code");
            alertE.setContentText("You have chosen the wrong code, try again !");
            alertE.showAndWait();
        }

    }

    @FXML
    void initialize() {
        assert resetCodeTextField != null : "fx:id=\"resetCodeTextField\" was not injected: check your FXML file 'ResetCode.fxml'.";

    }

}
