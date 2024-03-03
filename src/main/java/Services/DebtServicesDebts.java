package Services;

import Entities.Debt;
import Interfaces.IServicesDebts;
import Tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DebtServicesDebts implements IServicesDebts<Debt> {
    @Override
    public void addEntity(Debt debt) throws SQLException{
        String query = "INSERT INTO Debt (Amount, PaymentDate, AmountToPay,InterestRate, Type, CreationDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            String tyDette = getTypeDetteValue(debt.getType());

            if (tyDette == null) {
                System.out.println("Type de dette invalide.");
                return;
            }

            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(query);
            ps.setDouble(1, debt.getAmount());
            ps.setDate(2, debt.getPaymentDate());
            ps.setDouble(3, debt.getAmountToPay());
            ps.setFloat(4, debt.getInterestRate());
            ps.setString(5, debt.getType());
            ps.setDate(6, debt.getCreationDate());

            ps.executeUpdate();

            System.out.println("Dette ajoutée avec succès.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getTypeDetteValue(String typeDette) {
        String query = "SELECT NameDebt FROM DebtCategory WHERE NameDebt = ?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(query);
            ps.setString(1, typeDette);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("NameDebt");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void updateEntity(Debt debt) throws SQLException{
        String query = "UPDATE Debt SET Amount = ?, CreationDate = ?, PaymentDate = ?, AmountToPay = ?, InterestRate = ?, Type = ? WHERE IdDebt = ?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(query);
            ps.setDouble(1, debt.getAmount());
            ps.setDate(2, debt.getCreationDate());
            ps.setDate(3, debt.getPaymentDate());
            ps.setDouble(4, debt.getAmountToPay());
            ps.setFloat(5, debt.getInterestRate());
            ps.setString(6, debt.getType());
            ps.setInt(7, debt.getIdDebt());

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Dette mise à jour avec succès.");
            } else {
                System.out.println("Aucune dette mise à jour.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(Debt debt) {
        String query = "DELETE FROM Debt WHERE IdDebt = ?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(query);
            ps.setInt(1, debt.getIdDebt());

            int deletedRows = ps.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("Dette supprimée avec succès.");
            } else {
                System.out.println("Aucune dette supprimée.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Debt> getAllData() {
        List<Debt> data = new ArrayList<>();
        String requete = "SELECT * FROM Debt";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                Debt debt = new Debt();
                debt.setIdDebt(rs.getInt("IdDebt"));
                debt.setAmount(rs.getDouble("Amount"));
                debt.setPaymentDate(rs.getDate("PaymentDate"));
                debt.setAmountToPay(rs.getDouble("AmountToPay"));
                debt.setInterestRate(rs.getFloat("InterestRate"));
                debt.setType(rs.getString("Type"));
                debt.setCreationDate(rs.getDate("CreationDate"));
                data.add(debt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
}
