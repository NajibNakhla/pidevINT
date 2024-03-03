package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.IncomeType;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class Form2 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label idUserLabel;

    @FXML
    private ChoiceBox<IncomeType> incomeType;
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
    void next2(ActionEvent event) {
        us.addIncome(incomeType.getValue(),id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Form3.fxml"));

        try {
            Parent root = loader.load();
            Form3 pi = loader.getController();
            pi.setId(id);
            pi.setLabel(fName);
            idUserLabel.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void initialize() {
        incomeType.getItems().addAll(IncomeType.values());
        idUserLabel.setVisible(false);
        welcomeLabel.setVisible(false);
        this.setLabel(fName);
    }

}
