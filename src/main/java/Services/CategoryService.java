
package Services;

import Entities.Category;
import Entities.SubCategory;
import Tools.MyConnection;
import javafx.scene.control.Alert;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    public void addEntity(Category category) {
        String query = "INSERT INTO category (name, budgetLimit) VALUES (?, ?)";

        try {

            if (!isCategoryNameExists(category.getName())) {
                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
                pst.setString(1, category.getName());
                pst.setDouble(2, category.getBudgetLimit());

                pst.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", null, "Category added successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Category with the same name already exists!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public void updateEntity(String newName, int id) {
//        String query = "UPDATE category SET name=? WHERE idCategory=?";
//
//        try {
//            // Check if the new category name is unique before updating
//            if (!isCategoryNameExists(newName)) {
//                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
//                pst.setString(1, newName);
//                pst.setInt(2, id);
//
//                pst.executeUpdate();
//                showAlert(Alert.AlertType.INFORMATION, "Success", null, "Category name updated successfully");
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Error", null, "Category with the same name already exists!");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public void updateEntity(int id, String newName, Double newBudgetLimit) {
        String query = "UPDATE category SET name=?, budgetLimit=? WHERE idCategory=?";

        try {
            // Check if the new category name is unique before updating
            if (!isCategoryNameExists(newName)) {
                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
                pst.setString(1, newName);
                pst.setDouble(2, newBudgetLimit);
                pst.setInt(3, id);

                pst.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", null, "Category updated successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Category with the same name already exists!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //  method to check if a category name already exists in the database
    private boolean isCategoryNameExists(String name) {
        String query = "SELECT COUNT(*) FROM category WHERE name=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //  method to show alert dialogs
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void deleteEntityById(int categoryId) {
        String query = "DELETE FROM category WHERE idCategory=?";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, categoryId);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Category with ID " + categoryId + " deleted successfully");
            } else {
                System.out.println("No category found with ID " + categoryId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> getAllData() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";

        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("idCategory"));
                category.setName(rs.getString("name"));
                category.setBudgetLimit(rs.getDouble("budgetLimit"));
                category.setSubCategories(getSubCategoriesForCategory(category.getId()));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    public List<SubCategory> getSubCategoriesForCategory(int categoryId) {
        List<SubCategory> subCategories = new ArrayList<>();
        String query = "SELECT * FROM subcategory WHERE idCategory=?";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, categoryId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                SubCategory subCategory = new SubCategory();
                subCategory.setId(rs.getInt("id"));
                subCategory.setNom(rs.getString("nom"));
                subCategory.setMtAssigné(rs.getDouble("MtAssigné"));
                subCategory.setMtDépensé(rs.getDouble("MtDépensé"));
                subCategory.setIdCategory(rs.getInt("idCategory"));
                subCategories.add(subCategory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return subCategories;
    }
}