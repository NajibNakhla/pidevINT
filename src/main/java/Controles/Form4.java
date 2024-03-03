package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.Transport;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class Form4 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label idUserLabel;

    @FXML
    private ChoiceBox<Transport> transport;
    @FXML
    private Label welcomeLabel;

    private int id;
    private String fName;
    UserService us = new UserService();
    public void setLabel(String name){
        this.welcomeLabel.setId(name);
    }
    public void setId(int id) {
        this.id=id;
        idUserLabel.setId(String.valueOf(id));
    }

    @FXML
    void next4(ActionEvent event) {
        us.addTransport(transport.getValue(),id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Form5.fxml"));

        try {
            Parent root = loader.load();
            Form5 pi = loader.getController();
            pi.setId(id);
            pi.setLabel(fName);
            idUserLabel.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void initialize() {
        transport.getItems().addAll(Transport.values());
        idUserLabel.setVisible(false);
        welcomeLabel.setVisible(false);
        this.setLabel(fName);
    }

}
