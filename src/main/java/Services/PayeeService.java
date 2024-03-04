package Services;

import Entities.Payee;
import Interfaces.IPayee;
import Tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PayeeService implements IPayee<Payee> {

    @Override
    public void addEntity(Payee payee) {
        String requete = "INSERT INTO payee (namePayee) VALUES (?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, payee.getNamePayee());

            pst.executeUpdate();
            System.out.println("Personne ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    @Override
    public void updateEntity(Payee payee, int id) {
        String requete = "UPDATE  payee SET namePayee = ?  where idPayee=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, payee.getNamePayee());
            pst.setString(2, String.valueOf(id));



            pst.executeUpdate();
            System.out.println("Payee update successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deleteEntity(int id) {
        String requete = "DELETE FROM  payee where idPayee = ?  ";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, String.valueOf(id));
            pst.executeUpdate();
            System.out.println("Payee deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public List<Payee> getAllData() {
        List<Payee> data = new ArrayList<>();
        String requete = "SELECT *FROM payee" ;
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery(requete);
            while (rs.next()) {
                Payee p = new Payee();
                p.setidPayee(rs.getInt(1));
                p.setNamePayee(rs.getString("namePayee"));
                data.add(p);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public List<String> getPayeeNames() {
        String requete = "SELECT *FROM payee" ;
        List<String> payeeNames = new ArrayList<>();

        try  {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery(requete);

            while (rs.next()) {
                String payeeName = rs.getString("namePayee");
                payeeNames.add(payeeName);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return payeeNames;
    }


    public int getPayeeIdByName(String payeeName) {
        String requete = "SELECT idPayee FROM payee WHERE namePayee = ?";
        int payeeId = -1;

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, payeeName);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    payeeId = resultSet.getInt("idPayee");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return payeeId;
    }


    public boolean isPayeeNameUnique(String categoryName) {
        String query = "SELECT COUNT(*) FROM payee WHERE namePayee = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
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





