package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.BudgetingType;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class Form1 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Label idUserLabel;
    @FXML
    private ChoiceBox<BudgetingType> budgetType;
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
    public int getId(){
        return this.id;
    }

    @FXML
    void next1(ActionEvent event) {
        us.addBudgeting(budgetType.getValue(),id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Form2.fxml"));

        try {
            Parent root = loader.load();
            Form2 pi = loader.getController();
            pi.setId(id);
            pi.setLabel(fName);
            idUserLabel.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void initialize() {
        budgetType.getItems().addAll(BudgetingType.values());
        idUserLabel.setVisible(false);
        welcomeLabel.setVisible(false);
        this.setLabel(fName);

    }
}
