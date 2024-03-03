package Services;

import Entities.DebtCategory;
import Interfaces.IServicesDebts;
import Tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DebtCategoryServicesDebts implements IServicesDebts<DebtCategory> {
    @Override
    public void addEntity(DebtCategory debtCategory) throws SQLException{
        String requete = "INSERT INTO DebtCategory (NameDebt) " +
                "VALUES ('"+debtCategory.getNameDebt()+"')";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            st.executeUpdate(requete);
            System.out.println("Type ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(DebtCategory debtCategory) throws SQLException {
        String requete = "UPDATE DebtCategory SET NameDebt = ? WHERE NameDebt = ?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ps.setString(1, debtCategory.getNameDebt());
            ps.setString(2, debtCategory.getOldNameDebt()); // Use the same name for both parameters

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                debtCategory.setOldNameDebt(debtCategory.getNameDebt());
                System.out.println("Type de dette mis à jour avec succès.");
            } else {
                System.out.println("Impossible de mettre à jour le type de dette.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void deleteEntity(DebtCategory debtCategory) {
        String requete = "DELETE FROM DebtCategory WHERE NameDebt = '" + debtCategory.getNameDebt() + "'";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            int rowsDeleted = st.executeUpdate(requete);
            if (rowsDeleted > 0) {
                System.out.println("Type de dette supprimé avec succès.");
            } else {
                System.out.println("Impossible de trouver le type de dette à supprimer.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<DebtCategory> getAllData() {
        List<DebtCategory> data = new ArrayList<>();
        String requete = "SELECT * FROM DebtCategory";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                DebtCategory TyDette = new DebtCategory();
                TyDette.setNameDebt(rs.getString("NameDebt"));
                data.add(TyDette);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
}
