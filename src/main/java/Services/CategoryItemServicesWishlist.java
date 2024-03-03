package Services;

import Entities.CategoryItem;
import Interfaces.IServicesWishlist;
import Tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryItemServicesWishlist implements IServicesWishlist<CategoryItem> {
    @Override
    public void addEntity(CategoryItem categoryItem) {
        String requete = "INSERT INTO itemcategory (nameItemCategory) VALUES (?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, categoryItem.getNameItemCategory());
            pst.executeUpdate();
            System.out.println("Catégorie ajoutée!");
        } catch(SQLException e){
            System.out.println((e.getMessage()));
        }
    }

    @Override
    public void updateEntity(CategoryItem categoryItem, int id) {
        String requete = "UPDATE itemcategory SET nameItemCategory=? WHERE idItemCategory=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, categoryItem.getNameItemCategory());
            pst.setInt(2, id);
            pst.executeUpdate();
            System.out.println("Catégorie mise à jour !");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deleteEntity(int id) {
        String requete = "DELETE FROM itemcategory WHERE idItemCategory=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("Catégorie supprimée !");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<CategoryItem> getAllData() {
        List<CategoryItem> data = new ArrayList<>();
        String requete = "SELECT * FROM itemcategory";
        try {
            Statement st = MyConnection.getInstance(). getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                CategoryItem c = new CategoryItem();
                c.setIdItemCategory(rs.getInt(1));
                c.setNameItemCategory(rs.getString("nameItemCategory"));
                data.add(c);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return data;
    }
    public boolean isCategoryNameUnique(String categoryName) {
        String query = "SELECT COUNT(*) FROM itemcategory WHERE nameItemCategory = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, categoryName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // If count is 0, the name is unique
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false if an exception occurs
    }

}
