package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.User;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class Form5 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label idUserLabel;
    @FXML
    private ChoiceBox<Boolean> rent;
    @FXML
    private Label welcomeLabel;

    private int id;
    private String fName;
    UserService us = new UserService();
    public void setLabel(String name){
        this.welcomeLabel.setId("Welcome"+name);
    }
    public void setId(int id) {
        this.id=id;
        idUserLabel.setId(String.valueOf(id));
    }

    @FXML
    void next5(ActionEvent event) {
        us.addRent(rent.getValue(),id);
       User u= us.selectUserById(id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserProfile.fxml"));

        try {
            Parent root = loader.load();
            UserProfile pi = loader.getController();
            pi.setId(id);
            pi.setFName(u.getFirstName());
            pi.setLName(u.getLastName());
            pi.setEmailField(u.getEmail());

            idUserLabel.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void initialize() {
        rent.getItems().addAll(Boolean.FALSE,Boolean.TRUE);
        idUserLabel.setVisible(false);
        welcomeLabel.setVisible(false);
        this.setLabel(fName);


    }

}
