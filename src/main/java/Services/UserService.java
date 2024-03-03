package Services;

import Entities.BudgetingType;
import Entities.IncomeType;
import Entities.Transport;
import Entities.User;
import Interfaces.IServices;
import Interfaces.IUserService;
import Tools.MyConnection;
import Tools.PassSecurity;
import Tools.PassValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class UserService implements IServices<User>, IUserService {
    PassSecurity ps = new PassSecurity();
    PassValidator pv = new PassValidator();
    Scanner sc = new Scanner(System.in);
    @Override
    public void addEntity(User user) {
        String requete = "INSERT INTO user (first_name,last_name,email,hash,salt) " +
                "VALUES (?,?,?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPassword());
            System.out.println("Personne ajoutée !");
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public void register(User user) {
        byte[] salt = ps.generateSalt();
        // Hash the password with the salt
        if (emailExists(user.getEmail())) {
            System.out.println("User with email " + user.getEmail() + " already exists.");
            return; // Exit the method if email exists
        }
        String requete = "INSERT INTO user (first_name,last_name,email,hash,salt) " +
                "VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            String hashedPassword = ps.hashPassword(user.getPassword(), salt);
            pst.setString(4, hashedPassword);
            pst.setBytes(5, salt);

            System.out.println("User Logged in successfully !");
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public void updateEntity(User user) {
        String requete = "UPDATE user SET " +
                "first_name = ? , " +
                "last_name = ? ," +
                "email = ? " +
                "WHERE idUser = ? ";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            pst.setInt(4, user.getIdUser());
            System.out.println("user updated !");
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void updateEntity2(int id,String fName, String lName, String email) {

        String requete = "UPDATE user SET " +
                "first_name = ? , " +
                "last_name = ? ," +
                "email = ? " +
                "WHERE idUser = ? ";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            pst.setString(1, fName);
            pst.setString(2, lName);
            pst.setString(3, email);
            pst.setInt(4, id);
            System.out.println("user updated !");
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }




    @Override
    public void deleteEntity(User user) {


    }

    @Override
    public void deleteEntity(int id) {
        String requete ="DELETE FROM user" +
                " WHERE idUser = ? ";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1,id);
            System.out.println("user deleted !");
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<User> getAllData() {
        List<User> data = new ArrayList<>();
        String requete = "SELECT * FROM user";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                User u = new User();
                u.setIdUser(rs.getInt(1));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setEmail(rs.getString("email"));

                /*u.setDebt(rs.getBoolean("debt"));
                u.setBudgetType((BudgetingType) rs.getObject("budgetType"));
                u.setIncomeType((IncomeType) rs.getObject("incomeType"));
                u.setRent(rs.getBoolean("rent"));
                u.setRole(rs.getString("role"));
                u.setTransport((Transport) rs.getObject("transport"));*/
                data.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    @Override
    public void addTransport(Transport transport, int id) {
        String requete = "UPDATE user SET transport = ? WHERE idUser = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, String.valueOf(transport));
            pst.setInt(2, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addRent(Boolean bool,int id) {
        String requete = "UPDATE user SET rent = ? WHERE idUser = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setBoolean(1,bool );
            pst.setInt(2, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void addDebt(Boolean bool,int id) {
        String requete = "UPDATE user SET debt = ? WHERE idUser = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setBoolean(1,bool );
            pst.setInt(2, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addIncome(IncomeType incomeType,int id) {
        String requete = "UPDATE user SET incomeType = ? WHERE idUser = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, String.valueOf(incomeType));
            pst.setInt(2, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addBudgeting(BudgetingType budgetingType,int id) {
        String requete = "UPDATE user SET budgetType = ? WHERE idUser = ?";
       try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
           pst.setString(1, String.valueOf(budgetingType));
            pst.setInt(2, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    public boolean login2(String email, String password){
        User user = null;
        if(!emailExists(email)){
            System.out.println("email doesn't exist");
            return false;
        }
        String requete = "SELECT idUser, email, hash, salt FROM user WHERE email = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1,email);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                String hashedPass = rs.getString("hash");
                byte[] salt = rs.getBytes("salt");
                String myPassword= ps.hashPassword(password,salt);
                System.out.println(myPassword.equals(hashedPass));
                if(myPassword.equals(hashedPass)){
                    System.out.println("verifying");
                    user = new User();
                    user.setEmail(rs.getString("email"));
                    user.setEmail(rs.getString("idUser"));
                    System.out.println(user);
                    System.out.println("user logged in");
                    return true;
                }else {
                    System.out.println("wrong password");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;

    }


    //auth
    @Override
    public User login(String email, String password) {
        User user = null;
        if(!emailExists(email)){
            System.out.println("email doesn't exist");
            return null;
        }
        String requete = "SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1,email);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                byte[] salt = rs.getBytes("salt");
                String hashedPassword = rs.getString("hash");
                if (ps.validatePassword(password, hashedPassword, salt)) {
                     user = new User();
                    user.setEmail(email);
                    user.setIdUser(rs.getInt("idUser"));
                    user.setLastName(rs.getString("last_name"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setIncomeType(rs.getObject(6,IncomeType.class));
                    user.setBudgetType(rs.getObject(7,BudgetingType.class));
                    user.setRent(rs.getBoolean("rent"));
                    user.setDebt(rs.getBoolean("debt"));
                    user.setTransport(rs.getObject(10,Transport.class));
                    user.setUrlImage(rs.getString("urlImage"));
                    System.out.println("user logged in");
                    System.out.println(user);
                }else
                    System.out.println("wrong pass");
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return user;
    }

    //select user by id
    @Override
    public User selectUserById(int id) {
        User user =null;
        String requete = "SELECT idUser,first_name,last_name,email FROM user " +
                "WHERE idUser = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery(requete);
            if (rs.next()) {
                user = new User();
                pst.setInt(1,id);
               // user.setIdUser();
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }
    //verification de l'email
    @Override
    public boolean emailExists(String email){
        String requete = "SELECT COUNT(*) FROM user WHERE email = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, email);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count > 0, email exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        return false;
    }
    public void saveImageUrltoDatabase(int id, String imageUrl) {
        String requete="UPDATE user SET urlImage = ?" +
                "WHERE  idUser = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1,imageUrl);
            pst.setInt(2,id);
            pst.executeUpdate();
            System.out.println("image added");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int getUserId(String email){
        String requete = "SELECT idUser FROM user WHERE email = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, email);
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("idUser");
                } else {

                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public void comparePass(String email, String password){
        String req ="SELECT hash, salt from user where email = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setString(1,email);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                byte[] salt = rs.getBytes("salt");
                String hashedPassword = rs.getString("hash");
                String myPass = ps.hashPassword(password,salt);
                System.out.println(hashedPassword);
                System.out.println(myPass);
                System.out.println(ps.validatePassword(password,hashedPassword,salt)?"logged in":"not logged in");
            }

        } catch (SQLException e) {

        }


    }
    public void updateCode(String email, String resetCode) {
        try {

            String req = "UPDATE user SET resetCode = ? WHERE email = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setString(1, resetCode);
            pst.setString(2, email);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isValidResetCode(String resetCode){
        String req = "SELECT resetCode FROM user WHERE resetCode = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setString(1, resetCode);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                // If the reset code exists, return true
                return true;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public void updatePassword(String newPassword, String email){
        byte[] salt = ps.generateSalt();
        String req ="UPDATE user SET hash = ? ," +
                "salt = ? " +
                "WHERE email = ? ";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);

            pst.setString(3,email);
            String hashedPassword = ps.hashPassword(newPassword, salt);
            pst.setString(1,hashedPassword);
            pst.setBytes(2,salt);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
