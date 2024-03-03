package MyTests;

import com.google.gson.JsonObject;
import Entities.*;
//import entitiesZeydin.Wallet;
//import entitiesZeydin.WalletService;
import Services.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static Services.PriceAPIServices.fetchPriceAndAvailability;
import static Services.PriceAPIServices.postJob;

public class MainClass {
    public static void main(String[] args) throws Exception {
        // Create instances of services
        WishlistServicesWishlist ws = new WishlistServicesWishlist();
        WishlistItemServicesWishlist wis = new WishlistItemServicesWishlist();
        CategoryItemServicesWishlist ics = new CategoryItemServicesWishlist();
        WalletService walls= new WalletService();
      //  Wallet wallet = new Wallet("wallet1");
        //walls.addEntity(wallet);
        System.out.println("*****TEST CRUD WISHLIST******\n");
        // Create and add a new wishlist
        Wishlist wishlist = new Wishlist("My Wishlist", 500.0, 1);
        //ws.addEntity(wishlist);

        // Get all wishlists and print them
        //System.out.println("All Wishlists:");
        List<Wishlist> allWishlists = ws.getAllData();
        //for (Wishlist w : allWishlists) {
        //    System.out.println(w);
        //}

        // Create and add a new item category
        CategoryItem itemCategory = new CategoryItem("Electronics");
        //ics.addEntity(itemCategory);

        // Create and add a new wishlist item
        WishlistItem wishlistItem1 = new WishlistItem("Smartphone", 400.0, Priority.MEDIUM, Status.NOT_STARTED, 1,4);
        //wis.addEntity(wishlistItem1);
        wishlistItem1.setStatus(Status.IN_PROGRESS);
        wis.updateEntity(wishlistItem1,32);
        System.out.println(wis.getAllData());
        //wis.deleteEntity(31);
        // Get all wishlist items and print them
        //System.out.println("All Wishlist Items:");
        List<WishlistItem> allWishlistItems = wis.getAllData();
        //for (WishlistItem item : allWishlistItems) {
        //    System.out.println(item);
        //}

        // Update the wishlist
        wishlist.setMonthlyBudget(600.0);
        ws.updateEntity(wishlist,4);
        System.out.println("All Wishlists:");
        for (Wishlist w : allWishlists) {
            System.out.println(w);
        }
        //System.out.println(wis.getAllData());
        // Delete the wishlist item
        //wis.deleteEntity(3);

        // Delete the wishlist
        //ws.deleteEntity(5);

        //ws.addEntity(new Wishlist("my second wishlist",230));
        //ws.addWishlistItem(new WishlistItem());

        // Call the method and store the result
        System.out.println(ws.getSavedBudget(61));
        double progress = wis.calculateProgressForItemInProgress(61);
        // Check if the progress is valid (-1 indicates an error or invalid item ID)
        if (progress == -1.0) {
            System.out.println("Error: Unable to calculate progress for item with ID " +progress);
        } else {
            System.out.println("Progress for item with ID 61: " + progress + "%");
        }

        LocalDate purchaseDate = wis.estimatePurchaseDate(63);
        System.out.println("Estimated purchase date for item with ID 63: " + purchaseDate);

        LocalDate purchaseDate2 = wis.estimatePurchaseDate(45);
        System.out.println("Estimated purchase date for item with ID 45: " + purchaseDate2);

        //API TEST
        String term = wishlistItem1.getNameWishlistItem();
        PriceAPIServices priceAPIServices = new PriceAPIServices();
        try {
            JsonObject jobResponse = postJob(term);
            String jobId = jobResponse.get("job_id").getAsString();
            System.out.println("Job ID: " + jobId);

            // Fetch price and availability
            //fetchPriceAndAvailability(jobId);
            System.out.println(fetchPriceAndAvailability(jobId));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GMailerService gMailerService = new GMailerService();
        String subject = "\uD83C\uDF89 You Did It! Time to Treat Yourself to [Item] \uD83C\uDF89";
        String message = """
                Hey [User]!
                                
                Guess what? You've hit the jackpot! You've reached 100% progress toward your goal of snagging that awesome [Item] you've had your eye on. Woohoo! \uD83C\uDF89
                                
                Give yourself a pat on the back because all that hard work and determination have finally paid off. It's time to celebrate your success and treat yourself to something special.
                                
                Here's your personalized roadmap to nabbing your dream [Item]
                                
                Need a little extra pep in your step or some guidance along the way? Our fantastic customer support team is here to sprinkle some joy and assist you with anything you need.
                                
                So go ahead, unleash your inner shopping guru, and make that purchase! You've earned it, champ!
                                
                Keep shining bright,
                Farah Sayari
                Support Specialist
                Pennywise 
                                
                """;
        gMailerService.sendMail(subject, message);





    }

}
