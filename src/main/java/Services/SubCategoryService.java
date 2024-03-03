//package services;
//
//import entities.SubCategory;
//import interfaces.IServices;
//import utils.MyConnection;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SubCategoryService implements IServices<SubCategory> {
//
//    @Override
//    public void addEntity(SubCategory subcategory) {
//        String requete = "INSERT INTO subcategory (nom, MtAssigné, MtDépensé, idCategory) VALUES(?,?,?,?)";
//
//        try {
//            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
//            pst.setString(1, subcategory.getNom());
//            pst.setDouble(2, subcategory.getMtAssigné());
//            pst.setDouble(3, subcategory.getMtDépensé());
//            pst.setInt(4, subcategory.getIdCategory());
//
//            pst.executeUpdate();
//            System.out.println("Subcategory ajoutée");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void updateEntity(String newName, int id) {
//        String requete = "UPDATE subcategory SET nom=? WHERE id=?";
//
//        try {
//            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
//            pst.setString(1, newName);
//            pst.setInt(2, id);
//            pst.executeUpdate();
//            System.out.println("Subcategory name updated");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//
//    public void deleteEntityById(int id) {
//        String requete = "DELETE FROM subcategory WHERE id=?";
//
//        try {
//            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
//            pst.setInt(1, id); // Use the id parameter
//            pst.executeUpdate();
//            System.out.println("Subcategory deleted successfully");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public List<SubCategory> getAllData() {
//        List<SubCategory> data = new ArrayList<>();
//        String requete = "SELECT * FROM subcategory";
//        try {
//            Statement st = MyConnection.getInstance().getCnx().createStatement();
//            ResultSet rs = st.executeQuery(requete);
//            while (rs.next()) {
//                SubCategory c = new SubCategory();
//                c.setId(rs.getInt("id"));
//                c.setNom(rs.getString("nom"));
//                c.setMtAssigné(rs.getDouble("MtAssigné"));
//                c.setMtDépensé(rs.getDouble("MtDépensé"));
//                c.setIdCategory(rs.getInt("idCategory"));
//                data.add(c);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return data;
//    }
//
//
//
//}
package Services;

import Entities.SubCategory;
import Tools.MyConnection;
import javafx.scene.control.Alert;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryService {
    public void addEntity(SubCategory subcategory) {
        String requete = "INSERT INTO subcategory (nom, MtAssigné, MtDépensé, idCategory) VALUES(?,?,?,?)";

        try {
            // Check if the subcategory name is unique before adding
            if (!isSubCategoryNameExists(subcategory.getNom())) {
                // Check if both Montant Assigné and Montant Dépensé are specified
                if (subcategory.getMtAssigné() == 0 || subcategory.getMtDépensé() == 0) {
                    showAlert("Please specify both Montant Assigné and Montant Dépensé.");
                    return; // Exit the method if either Montant Assigné or Montant Dépensé is not specified
                }

                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
                pst.setString(1, subcategory.getNom());
                pst.setDouble(2, subcategory.getMtAssigné());
                pst.setDouble(3, subcategory.getMtDépensé());
                pst.setInt(4, subcategory.getIdCategory());

                pst.executeUpdate();
                System.out.println("Subcategory added successfully");
            } else {
                showAlert("Subcategory with the same name already exists!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEntity(String newName, int id) {
        String requete = "UPDATE subcategory SET nom=? WHERE id=?";

        try {
            // Check if the new subcategory name is unique before updating
            if (!isSubCategoryNameExists(newName)) {
                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
                pst.setString(1, newName);
                pst.setInt(2, id);
                pst.executeUpdate();
                System.out.println("Subcategory name updated");
            } else {
                showAlert("Subcategory with the same name already exists!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMontantDepense(int id,double Montant ) {
        String requete = "UPDATE subcategory SET MtDépensé=? WHERE id=?";

        try {
                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
                pst.setDouble(1, Montant);
                pst.setInt(2, id);
                pst.executeUpdate();
                System.out.println("Subcategory Montant dépensé");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to check if a subcategory name already exists in the database
    private boolean isSubCategoryNameExists(String name) {
        String query = "SELECT COUNT(*) FROM subcategory WHERE nom=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count is greater than 0, the name already exists
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

//    public void addEntity(SubCategory subcategory) {
//        String requete = "INSERT INTO subcategory (nom, MtAssigné, MtDépensé, idCategory) VALUES(?,?,?,?)";
//
//        try {
//            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
//            pst.setString(1, subcategory.getNom());
//            pst.setDouble(2, subcategory.getMtAssigné());
//            pst.setDouble(3, subcategory.getMtDépensé());
//            pst.setInt(4, subcategory.getIdCategory());
//
//            pst.executeUpdate();
//            System.out.println("Subcategory ajoutée");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void updateEntity(String newName, int id) {
//        String requete = "UPDATE subcategory SET nom=? WHERE id=?";
//
//        try {
//            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
//            pst.setString(1, newName);
//            pst.setInt(2, id);
//            pst.executeUpdate();
//            System.out.println("Subcategory name updated");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void deleteEntityById(int id) {
        String requete = "DELETE FROM subcategory WHERE id=?";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Subcategory deleted successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SubCategory> getAllData() {
        List<SubCategory> data = new ArrayList<>();
        String requete = "SELECT * FROM subcategory";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                SubCategory c = new SubCategory();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setMtAssigné(rs.getDouble("MtAssigné"));
                c.setMtDépensé(rs.getDouble("MtDépensé"));
                c.setIdCategory(rs.getInt("idCategory"));
                data.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
