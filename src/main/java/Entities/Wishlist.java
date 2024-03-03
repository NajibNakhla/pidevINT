package Entities;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Wishlist {
    private int idWishlist;
    private String nameWishlist;
    private double monthlyBudget;
    private LocalDate creationDate;
    private int idWallet;
    private double savedBudget;

    private List<WishlistItem> wishlistItems;

    public double getSavedBudget() {
        return savedBudget;
    }

    public void setSavedBudget(double savedBudget) {
        this.savedBudget = savedBudget;
    }

    public Wishlist() {
    }

    public Wishlist(String nameWishlist, double monthlyBudget, int idWallet) {
        this.nameWishlist = nameWishlist;
        this.monthlyBudget = monthlyBudget;
        this.idWallet = idWallet;
        wishlistItems = new ArrayList<>();

    }

    public Wishlist(String nameWishlist, double monthlyBudget) {
        this.nameWishlist = nameWishlist;
        this.monthlyBudget = monthlyBudget;
    }

    public int getIdWishlist() {
        return idWishlist;
    }

    public void setIdWishlist(int idWishlist) {
        this.idWishlist = idWishlist;
    }

    public String getNameWishlist() {
        return nameWishlist;
    }

    public void setNameWishlist(String nameWishlist) {
        this.nameWishlist = nameWishlist;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    public List<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistItems(List<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "idWishlist=" + idWishlist +
                ", nameWishlist='" + nameWishlist + '\'' +
                ", monthlyBudget=" + monthlyBudget +
                ", creationDate=" + creationDate +
                ", idWallet=" + idWallet +
                ", wishlistItems=" + wishlistItems +
                '}';
    }
}
