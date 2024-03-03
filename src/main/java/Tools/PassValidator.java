package Tools;

import javafx.scene.control.Alert;

public class PassValidator {
    public PassValidator() {
    }
    public static boolean isStrongPassword(String password){
        if (password.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password should be longer");
            return false;
        }
        // Verifier que le mdp contient une seule lettre maj
        if (!password.matches(".*[A-Z].*")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password should contain at least one Uppercase letter");
            return false;
        }
        // Verifier que le mdp contient une seule lettre min
        if (!password.matches(".*[a-z].*")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password should contain at least one lowercase letter");
            return false;
        }
        // Verifier que le mdp contient des caractéres spéciaux
        /*if (!password.matches(".*[!@#$%^&*()-_=+\\|[{]};:'\",<.>/?`~].*")) {
            System.out.println("Password must contain at least one special character");
            return false;
        }*/
        return true;
    }
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
