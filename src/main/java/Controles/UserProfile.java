package Controles;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.UUID;

import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class UserProfile {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField emailField;

    @FXML
    private TextField fNameField;

    @FXML
    private TextField lNameField;

    @FXML
    private ImageView userImage;
    private File photoFile;
    @FXML
    private Label idUserLabel;
    private int id;
    UserService us = new UserService();
    public void setId(int id) {
        this.id=id;
        idUserLabel.setId(String.valueOf(id));
    }
    public void setFName(String name){
        this.fNameField.setText(name);
    }
    public void setLName(String lname){
        this.lNameField.setText(lname);
    }
    public void setEmailField(String email){
        this.emailField.setText(email);
    }

    @FXML
    void update(ActionEvent event) {

        if(id!=0) {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setTitle("Confirmation");
            alert2.setHeaderText("Update Account");
            alert2.setContentText("Are you sure you want to update your account ?");
            alert2.showAndWait();
            us.updateEntity2(id,fNameField.getText(),lNameField.getText(),emailField.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("User updated");
            alert.show();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserProfile.fxml"));
            try {
                Parent root = loader.load();
                UserProfile pi = loader.getController();
                pi.setId(id);
                lNameField.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not update user");
            alert.show();
        }
    }
    @FXML
    void deleteAccount(ActionEvent event) {
        System.out.println(id);
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("Confirmation");
        alert2.setHeaderText("Delete Account");
        alert2.setContentText("Are you sure you want to delete your account ?");
        alert2.showAndWait();
        us.deleteEntity(id);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("User deleted");
        alert.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Home.fxml"));

        try {
           Parent root = loader.load();
            Home pi = loader.getController();
            lNameField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void UploadPhoto(ActionEvent event) {

        try {
            String imageUrl = saveImage(photoFile);
            if (imageUrl != null){
                us.saveImageUrltoDatabase(id, imageUrl);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Image uploaded successfully");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error uploading image.");
                alert.show();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error uploading image: " + e.getMessage());
            alert.show();
            e.printStackTrace(); // Log the exception stack trace
        }
    }

    @FXML
    void choosePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose photo");
        photoFile = fileChooser.showOpenDialog(null);
        if (photoFile != null) {
            try {
                Image image = new Image(photoFile.toURI().toString());
                userImage.setImage(image);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error loading image: " + e.getMessage());
                alert.show();
                e.printStackTrace(); // Log the exception stack trace
            }
        }
    }

    private String saveImage(File file) throws IOException{
        if (file == null) {
            return null; // No file selected
        }
        String imageName = UUID.randomUUID().toString() + ".png";
        Path imagePath = Paths.get("image_dossier", imageName);
        Files.createDirectories(imagePath.getParent()); // Create parent directories if they don't exist
        Files.copy(file.toPath(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        return imagePath.toUri().toString();

    }


    @FXML
    void resetAccount(ActionEvent event) {

    }



    @FXML
    void initialize() {
        idUserLabel.setVisible(false);
       // User user = us.selectUserById(id);
        //emailField.setText(user.getEmail());
       // lNameField.setText(user.getLastName());
      //  fNameField.setText(user.getFirstName());
        System.out.println(id);

    }

}
