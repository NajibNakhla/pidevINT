package Services;

import Entities.Budget;
import Interfaces.IBudget;
import Tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetService implements IBudget<Budget> {

    @Override
    public void addEntity(Budget budget) {
        String requete = "INSERT INTO budget (totalBudget,readyToAssign) VALUES (?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, String.valueOf(budget.getTotalBudget()));
            pst.setString(2, String.valueOf(budget.getReadyToAssign()));


            pst.executeUpdate();
            System.out.println("Account added successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Budget getData() {
        Budget b = new Budget();
        String requete = "SELECT * FROM Budget " ;
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery(requete);

            while (rs.next()) {
                b.setIdBudget(rs.getInt(1));
                b.setTotalBudget(rs.getDouble(2));
                b.setReadyToAssign(rs.getDouble(3));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return b;
    }
}
