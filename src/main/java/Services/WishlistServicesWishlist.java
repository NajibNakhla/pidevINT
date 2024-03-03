package Services;

import Entities.Priority;
import Entities.Wishlist;
import Entities.WishlistItem;
import Interfaces.IServicesWishlist;
import Tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//1. CRUD
public class WishlistServicesWishlist implements IServicesWishlist<Wishlist> {
    @Override
    public void addEntity(Wishlist wishlist) {
        String query = "INSERT INTO wishlist (nameWishlist, MonthlyBudget) VALUES (?, ?)";
        try {
            System.out.println(wishlist.getIdWallet());
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, wishlist.getNameWishlist());
            pst.setDouble(2, wishlist.getMonthlyBudget());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                wishlist.setIdWishlist(rs.getInt(1));
            }
            System.out.println("Wishlist added!");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(Wishlist wishlist, int id) {
        String query = "UPDATE wishlist SET nameWishlist=?, MonthlyBudget=? WHERE idWishlist=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, wishlist.getNameWishlist());
            pst.setDouble(2, wishlist.getMonthlyBudget());
            pst.setInt(3, id);
            pst.executeUpdate();
            System.out.println("Wishlist updated!");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int id) {
        String query = "DELETE FROM wishlist WHERE idWishlist=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Wishlist deleted!");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Wishlist> getAllData() {
        List<Wishlist> wishlists = new ArrayList<>();
        String query = "SELECT * FROM wishlist";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Wishlist wishlist = new Wishlist();
                wishlist.setIdWishlist(rs.getInt("idWishlist"));
                wishlist.setNameWishlist(rs.getString("nameWishlist"));
                wishlist.setMonthlyBudget(rs.getDouble("MonthlyBudget"));
                wishlist.setCreationDate(rs.getDate("creationDate").toLocalDate());
                wishlist.setIdWallet(rs.getInt("idWallet"));
                wishlists.add(wishlist);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return wishlists;
    }

    public Wishlist getWishlist(int wishlistId) {
        Wishlist wishlist = null;
        String query = "SELECT * FROM wishlist WHERE idWishlist=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, wishlistId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                wishlist = new Wishlist();
                wishlist.setIdWishlist(rs.getInt("idWishlist"));
                wishlist.setNameWishlist(rs.getString("nameWishlist"));
                wishlist.setMonthlyBudget(rs.getDouble("MonthlyBudget"));
                wishlist.setCreationDate(rs.getDate("creationDate").toLocalDate());
                wishlist.setIdWallet(rs.getInt("idWallet"));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return wishlist;
    }

    // 2. LIST WISHLIST CONTAINS ITEMS
    private WishlistItemServicesWishlist wishlistItemServices;
    public WishlistServicesWishlist() {
        this.wishlistItemServices = new WishlistItemServicesWishlist();
    }
    // Method to retrieve a single wishlist along with its items
    public Wishlist getWishlistWithItems(int wishlistId) {
        Wishlist wishlist = getWishlist(wishlistId); // Retrieve the wishlist from the database

        // If wishlist exists, retrieve its items
        if (wishlist != null) {
            List<WishlistItem> items = wishlistItemServices.getAllWishlistItems(wishlistId); // Retrieve items for the wishlist
            wishlist.setWishlistItems(items); // Set the items for the wishlist
        }

        return wishlist;
    }


    // Method to add a new wishlist item to a wishlist
    public void addWishlistItem(WishlistItem wishlistItem) {
        wishlistItemServices.addEntity(wishlistItem); // Add the wishlist item
    }

    public List<WishlistItem> getWishlistItemsSortedByPriority(int wishlistId) {
        List<WishlistItem> wishlistItems = new ArrayList<>();
        String query = "SELECT * FROM wishlistitem WHERE idWishlist = ? ORDER BY FIELD(priority, 'High', 'Medium', 'Low')";
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
                wishlistItem.setEndDate(rs.getDate("endDate").toLocalDate());
                wishlistItem.setPriority(Priority.valueOf(rs.getString("priority")));
                wishlistItem.setIdItemCategory(rs.getInt("idItemCategory"));
                wishlistItem.setIdWishlist(rs.getInt("idWishlist"));
                wishlistItems.add(wishlistItem);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return wishlistItems;
    }
    public List<String> getAllWishlistNames() {
        List<String> wishlistNames = new ArrayList<>();
        String query = "SELECT nameWishlist FROM wishlist";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                wishlistNames.add(rs.getString("nameWishlist"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return wishlistNames;
    }

    // Method to retrieve the ID of a wishlist by its name
    public int getWishlistIdByName(String wishlistName) {
        int wishlistId = -1; // Default value in case wishlist is not found
        String query = "SELECT idWishlist FROM wishlist WHERE nameWishlist = ?";
        PreparedStatement pst = null;
        try {
            pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, wishlistName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                wishlistId = rs.getInt("idWishlist");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return wishlistId;
    }

    public double getMonthlyBudget(int wishlistId) {
        String query = "SELECT MonthlyBudget FROM wishlist WHERE idWishlist = ?";
        double monthlyBudget = 0.0; // Default value

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, wishlistId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                monthlyBudget = rs.getDouble("MonthlyBudget");
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching monthly budget: " + e.getMessage());
        }

        return monthlyBudget;
    }

    public boolean isNameUnique(String wishlistName) {
        String query = "SELECT COUNT(*) FROM wishlist WHERE nameWishlist = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, wishlistName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // If count is 0, the name is unique
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false in case of exception or no result
    }

    public double getSavedBudget(int wishlistId) {
        double savedbudget;
        /*
        // Write SQL query to retrieve the saved budget for the specified wishlist
        String query = "SELECT savedBudget FROM wishlist WHERE idWishlist = ?";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, wishlistId);
            ResultSet rs = pst.executeQuery();
                // Check if there are results before retrieving the saved budget
                if (rs.next()) {
                    // Retrieve the saved budget from the result set
                    savedbudget = rs.getDouble("savedBudget");
                } else {
                    // Handle the case where no saved budget is found
                    savedbudget = 0.0; // or throw an exception, depending on your application logic
                }

        } catch (SQLException ex) {
            // Handle the exception appropriately, such as logging the error
            ex.printStackTrace();
            savedbudget = 0.0; // or throw an exception, depending on your application logic
        }

         */

        return 100.0;
    }







}

// Importez la bibliothèque pour faciliter les appels à l'API Amazon Product Advertising
/*
import com.amazon.paapi5.v1.ApiClient;
import com.amazon.paapi5.v1.ApiException;
import com.amazon.paapi5.v1.ItemApi;
import com.amazon.paapi5.v1.model.GetItemsRequest;
import com.amazon.paapi5.v1.model.GetItemsResponse;
import com.amazon.paapi5.v1.model.Item;
import com.amazon.paapi5.v1.model.Price;
*/

//CRUD

//Calculateur de budget
   /* public double calculateTotalRequiredBudget() {
        double totalRequiredBudget = 0.0;
        for (WishlistItem item : items) {
            totalRequiredBudget += item.getPrice();
        }
        return totalRequiredBudget;
    }
    /*
    * Previous Method (calculateSavingsProgress()):

    Calculates the savings progress based on the total required budget and the monthly budget, assuming the user consistently saves the expected amount each month.
    Does not take into account the actual savings made by the user.
    New Method (calculateSavingsProgress(double savedAmount)):

    Calculates the savings progress based on the remaining budget after deducting the actual amount saved by the user.
    Takes into account the actual savings made by the user, allowing for more accurate progress tracking.*/

// Méthode pour suivre la progression de l'épargne
    /*public double calculateSavingsProgress() {
        // Si le budget mensuel est 0 ou négatif, renvoyer 0 pour éviter une division par zéro
        if (monthlyBudget <= 0) {
            return 0.0;
        }

        // Calculer le nombre de mois nécessaires pour atteindre le budget total nécessaire
        double monthsNeeded = totalRequiredBudget / monthlyBudget;

        // Calculer le temps écoulé depuis la création de la liste de souhaits en mois
        long currentTime = System.currentTimeMillis();
        long creationTime = creationDate.getTime();
        double elapsedMonths = (currentTime - creationTime) / (30.44 * 24 * 60 * 60 * 1000); // 30.44 jours par mois en moyenne

        // Calculer la progression réalisée en pourcentage
        double progress = (elapsedMonths / monthsNeeded) * 100;
        return Math.min(progress, 100.0); // Limiter le pourcentage à 100%
    }
    public double calculateSavingsProgress(double savedAmount) {
        if (monthlyBudget <= 0) {
            return 0.0;
        }

        // Calculer le montant total nécessaire en soustrayant le montant réellement épargné du budget total initial
        double remainingBudget = totalRequiredBudget - savedAmount;

        // Calculer le nombre de mois nécessaires pour atteindre le montant total nécessaire avec le montant mensuel prévu
        double monthsNeeded = remainingBudget / monthlyBudget;

        // Calculer la progression réalisée en pourcentage
        double elapsedMonths = (System.currentTimeMillis() - creationDate.getTime()) / (30.44 * 24 * 60 * 60 * 1000);
        double progress = (elapsedMonths / monthsNeeded) * 100;

        return Math.min(progress, 100.0);
    }

    // Méthode pour mettre à jour les prix des articles depuis l'API Amazon Product Advertising
    //Intégrer l'API Amazon Product Advertising pour récupérer les prix des articles de la liste de souhaits à partir d'Amazon.
    //pour récupérer et mettre à jour les prix des articles de la liste de souhaits à partir de l'API Amazon Product Advertising.

    public void updatePricesFromAmazonAPI() {
        ApiClient client = new ApiClient();
        client.setAccessKey(ACCESS_KEY);
        client.setSecretKey(SECRET_KEY);

        ItemApi api = new ItemApi(client);

        for (WishlistItem item : items) {
            try {
                GetItemsRequest request = new GetItemsRequest();
                request.setItemIds(java.util.Arrays.asList(item.getAmazonItemId())); // Supposons que chaque item a un identifiant Amazon unique

                GetItemsResponse response = api.getItems(request);
                Item amazonItem = response.getItemsResult().getItems().get(0);

                // Récupérez le prix de l'élément depuis la réponse de l'API
                Price price = amazonItem.getPrice();
                double updatedPrice = Double.parseDouble(price.getDisplayAmount());

                // Mettez à jour le prix de l'article dans la liste de souhaits
                item.setPrice(updatedPrice);
            } catch (ApiException e) {
                System.err.println("Erreur lors de la mise à jour du prix pour l'article: " + e.getMessage());
            }
        }
    }
    public int estimateTimeToSave(double monthlySavingAmount) {
        // Calcul du montant total nécessaire pour acheter tous les articles de la liste de souhaits
        double totalRequiredBudget = calculateTotalRequiredBudget();
        // Estimation du temps nécessaire en fonction du montant économisé chaque mois
        return (int) Math.ceil(totalRequiredBudget / monthlySavingAmount);
    }


}
*/


