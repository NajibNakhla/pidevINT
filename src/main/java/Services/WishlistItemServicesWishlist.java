package Services;

import Entities.Priority;
import Entities.Status;
import Entities.WishlistItem;
import Interfaces.IServicesWishlist;
import Tools.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class WishlistItemServicesWishlist implements IServicesWishlist<WishlistItem> {
    @Override
    public void addEntity(WishlistItem wishlistItem) {
        String requete = "INSERT INTO wishlistItem (nameWishlistItem, price, priority, status, idItemCategory, idWishlist) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1,wishlistItem.getNameWishlistItem());
            pst.setDouble(2,wishlistItem.getPrice());
            pst.setString(3,wishlistItem.getPriority().toString());
            pst.setString(4,wishlistItem.getStatus().toString());
            pst.setInt(5,wishlistItem.getIdItemCategory());
            pst.setInt(6,wishlistItem.getIdWishlist());
            pst.executeUpdate();
            System.out.println("Wishlist Item ajoutée!");
        } catch(SQLException e){
            System.out.println((e.getMessage()));
        }
    }
    @Override
    public void updateEntity(WishlistItem wishlistItem, int id) {
        String requete = "UPDATE wishlistitem SET nameWishlistItem=?, price=?, priority=?, status=?,idItemCategory=? WHERE idWishlistItem=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, wishlistItem.getNameWishlistItem());
            pst.setDouble(2,wishlistItem.getPrice());
            pst.setString(3, wishlistItem.getPriority().toString());
            pst.setString(4,wishlistItem.getStatus().toString());
            pst.setInt(5,wishlistItem.getIdItemCategory());
            pst.setInt(6, id);
            pst.executeUpdate();
            System.out.println("Wishlist Item mise à jour !");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    public void deleteEntity(int id) {
        String requete = "DELETE FROM wishlistitem WHERE idWishlistItem=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("Wishlist Item supprimée !");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT nameItemCategory FROM itemCategory";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String category = rs.getString("nameItemCategory");
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching categories: " + e.getMessage());
        }

        return categories;
    }

    // Method to retrieve the category ID based on the category name
    public int getCategoryID(String categoryName) {
        int categoryId = -1; // Default value in case category is not found
        String query = "SELECT idItemCategory FROM itemCategory WHERE nameItemCategory = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1,categoryName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                //CategoryItem categoryItem = new CategoryItem();
                categoryId=rs.getInt("idItemCategory");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return categoryId;
    }

    public String getCategoryNameById(int categoryId) {
        String categoryName = null;
        String query = "SELECT nameItemCategory FROM itemCategory WHERE idItemCategory = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, categoryId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                categoryName = rs.getString("nameItemCategory");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving category name: " + e.getMessage());
        }
        return categoryName;
    }

    @Override
    public List<WishlistItem> getAllData() {
        List<WishlistItem> data = new ArrayList<>();
        String requete = "SELECT * FROM wishlistitem";
        try {
            Statement st = MyConnection.getInstance(). getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                WishlistItem w = new WishlistItem();
                w.setIdWishlistItem(rs.getInt(1));
                w.setNameWishlistItem(rs.getString("nameWishlistItem"));
                w.setPrice(rs.getDouble("price"));
                w.setPriority(Priority.valueOf(rs.getString("priority")));
                w.setStatus(Status.valueOf(rs.getString("status")));
                w.setCreationDate(rs.getDate("creationDate").toLocalDate());
                w.setIdItemCategory(rs.getInt("idItemCategory"));
                w.setIdWishlist(rs.getInt("idWishlist"));
                data.add(w);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return data;

    }

    public List<WishlistItem> getAllWishlistItems(int wishlistId) {
        List<WishlistItem> wishlistItems = new ArrayList<>();
        String query = "SELECT * FROM wishlistitem WHERE idWishlist=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, wishlistId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                WishlistItem wishlistItem = new WishlistItem();
                wishlistItem.setIdWishlistItem(rs.getInt("idWishlistItem"));
                wishlistItem.setNameWishlistItem(rs.getString("nameWishlistItem"));
                wishlistItem.setPrice(rs.getDouble("price"));
                wishlistItem.setMonthlyPay(rs.getDouble("monthlyPay"));
                wishlistItem.setCreationDate(rs.getDate("creationDate").toLocalDate());
                wishlistItem.setPriority(Priority.valueOf(rs.getString("priority")));
                wishlistItem.setStatus(Status.valueOf(rs.getString("status")));
                wishlistItem.setIdItemCategory(rs.getInt("idItemCategory"));
                wishlistItem.setIdWishlist(rs.getInt("idWishlist"));
                wishlistItems.add(wishlistItem);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return wishlistItems;
    }

    public boolean isItemUnique(WishlistItem newItem) {
        String itemName = newItem.getNameWishlistItem();
        double itemPrice = newItem.getPrice();
        int categoryId = newItem.getIdItemCategory();
        int wishlistId = newItem.getIdWishlist();

        String query = "SELECT COUNT(*) FROM wishlistitem WHERE nameWishlistItem = ? AND price = ? AND idItemCategory = ? AND idWishlist = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, itemName);
            pst.setDouble(2, itemPrice);
            pst.setInt(3, categoryId);
            pst.setInt(4, wishlistId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // If count is 0, item is unique; otherwise, it already exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Return false by default if an exception occurs
    }
    public int getWishlistID(String wishlistName) {
        String query = "SELECT idWishlist FROM wishlist WHERE nameWishlist = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, wishlistName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("idWishlist");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1; // Return -1 if wishlist with the given name is not found
    }




    public double calculateProgressForItemInProgress(int itemId) throws Exception {
        // Initialize progress to -1 indicating an error or invalid item ID
        double progress = -1.0;

        // Write SQL query to select the item by its ID and check if it's in progress
        String query = "SELECT price, nameWishlistItem, idWishlist FROM wishlistitem WHERE idWishlistItem = ? AND status = 'IN_PROGRESS'";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, itemId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double price = rs.getDouble("price");
                String nameWishlistItem = rs.getString("nameWishlistItem");
                int wishlistId = rs.getInt("idWishlist");

                // Get the saved budget of the wishlist
                WishlistServicesWishlist wishlistServices = new WishlistServicesWishlist();
                double savedBudget = wishlistServices.getSavedBudget(wishlistId);

                // Calculate progress
                progress =Math.round((float) ((savedBudget / price) * 100));
                progress = Math.min(progress, 100);
                updateProgress(itemId, progress);


        if (progress==100 && !isEmailSent(itemId)){
            GMailerService gMailerService = new GMailerService();
            updateProgress(itemId, 100);
            gMailerService.sendMail("\uD83C\uDF89 You Did It! Time to Treat Yourself to "+nameWishlistItem+"\uD83C\uDF89 ","""
        Hey !
                                
        Guess what? You've hit the jackpot! You've reached 100% progress toward your goal of snagging that awesome [Item] you've had your eye on. Woohoo! \uD83C\uDF89
                                
        Give yourself a pat on the back because all that hard work and determination have finally paid off. It's time to celebrate your success and treat yourself to something special.
                                
        Need a little extra pep in your step or some guidance along the way? Our fantastic customer support team is here to sprinkle some joy and assist you with anything you need.
                                
        So go ahead, unleash your inner shopping guru, and make that purchase! You've earned it, champ!
                                
        Keep shining bright,
        Farah Sayari
        Support Specialist
        Pennywise 
                                
        """);
            updateEmailSentFlag(itemId);
            return 100;


        }

    }} catch (Exception e) {
            throw new RuntimeException(e);
        }
        return progress;
    }


        public LocalDate estimatePurchaseDate(int itemId) {
        // Write SQL query to select the price, monthly budget, and saved budget for the item's wishlist
        String query = "SELECT wi.price, w.MonthlyBudget, w.savedBudget " +
                "FROM wishlistitem wi " +
                "JOIN wishlist w ON wi.idWishlist = w.idWishlist " +
                "WHERE wi.idWishlistItem = ? AND wi.status = 'IN_PROGRESS'";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, itemId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double itemPrice = rs.getDouble("price");
                double savedBudget = rs.getDouble("savedBudget");
                double monthlyBudget = rs.getDouble("MonthlyBudget");

                // Calculate the remaining amount needed to purchase the item
                double remainingAmount = itemPrice - savedBudget;
                // Calculate the number of months needed to save enough money
                int monthsNeeded = (int) Math.ceil(remainingAmount / monthlyBudget);
                LocalDate currentDate = LocalDate.now();
                if (monthsNeeded >= 1){
                    // Calculate the estimated purchase date

                    return currentDate.plusMonths(monthsNeeded);
                }
                else {
                    return currentDate;
                }

            } else {
                throw new IllegalArgumentException("Item with ID " + itemId + " not found or not in progress.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    private void updateProgress(int itemId, double progress) {
        // Write SQL query to update the progress in the database
        String updateQuery = "UPDATE wishlistitem SET progress = ? WHERE idWishlistItem = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(updateQuery);
            pst.setDouble(1, progress);
            pst.setInt(2, itemId);
            pst.executeUpdate();
            System.out.println("Progress updated successfully for item with ID: " + itemId);
        } catch (SQLException e) {
            System.out.println("Failed to update progress for item with ID: " + itemId);
            e.printStackTrace();
        }
    }
    public boolean isEmailSent(int itemId) {
        String query = "SELECT email_sent FROM wishlistitem WHERE idWishlistItem = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, itemId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int emailSent = rs.getInt("email_sent");
                return (emailSent == 1); // Return true if email has been sent (email_sent = 1), false otherwise
            }
        } catch (SQLException e) {
            System.out.println("Error checking if email has been sent for item with ID: " + itemId);
            e.printStackTrace();
        }
        return false; // Default to false if there's an error or if the item ID is not found
    }

    public void updateEmailSentFlag(int itemId) {
        String updateQuery = "UPDATE wishlistitem SET email_sent = 1 WHERE idWishlistItem = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(updateQuery);
            pst.setInt(1, itemId);
            pst.executeUpdate();
            System.out.println("Email sent flag updated successfully for item with ID: " + itemId);
        } catch (SQLException e) {
            System.out.println("Failed to update email sent flag for item with ID: " + itemId);
            e.printStackTrace();
        }
    }






}
