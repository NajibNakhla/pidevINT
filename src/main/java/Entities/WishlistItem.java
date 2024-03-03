package Entities;

import java.time.LocalDate;


public class WishlistItem {
    private int idWishlistItem;
    private String nameWishlistItem;
    private double price;
    private double monthlyPay;
    private LocalDate creationDate; // This will be set automatically
    private LocalDate endDate;
    private Priority priority;
    private int idItemCategory;
    private int idWishlist;
    private Status status;

    private double progress;
    private Byte email_sent;

    public Byte getEmail_sent() {
        return email_sent;
    }

    public void setEmail_sent(Byte email_sent) {
        this.email_sent = email_sent;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public WishlistItem() {
    }

    public WishlistItem(String nameWishlistItem, double price, double monthlyPay, Priority priority, int idItemCategory, int idWishlist) {
        this.nameWishlistItem = nameWishlistItem;
        this.price = price;
        this.monthlyPay = monthlyPay;
        this.priority = priority;
        this.idItemCategory = idItemCategory;
        this.idWishlist = idWishlist;
    }

    public WishlistItem(String nameWishlistItem, double price, Priority priority, Status status, int idItemCategory, int idWishlist) {
        this.nameWishlistItem = nameWishlistItem;
        this.price = price;
        this.priority = priority;
        this.status=status;
        this.idItemCategory = idItemCategory;
        this.idWishlist=idWishlist;
    }

    public WishlistItem(String nameWishlistItem, double price, LocalDate endDate, Priority priority, int idItemCategory) {
        this.nameWishlistItem = nameWishlistItem;
        this.price = price;
        this.endDate = endDate;
        this.priority = priority;
        this.idItemCategory = idItemCategory;
    }

    public WishlistItem(String nameWishlistItem, double price, Priority priority, int idItemCategory) {
        this.nameWishlistItem = nameWishlistItem;
        this.price = price;
        this.priority = priority;
        this.idItemCategory = idItemCategory;
    }
    public WishlistItem(String nameWishlistItem, double price) {
        this.nameWishlistItem = nameWishlistItem;
        this.price = price;
    }

    public int getIdWishlistItem() {
        return idWishlistItem;
    }

    public void setIdWishlistItem(int idWishlistItem) {
        this.idWishlistItem = idWishlistItem;
    }

    public String getNameWishlistItem() {
        return nameWishlistItem;
    }

    public void setNameWishlistItem(String nameWishlistItem) {
        this.nameWishlistItem = nameWishlistItem;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMonthlyPay() {
        return monthlyPay;
    }

    public void setMonthlyPay(double monthlyPay) {
        this.monthlyPay = monthlyPay;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getIdItemCategory() {
        return idItemCategory;
    }

    public void setIdItemCategory(int idItemCategory) {
        this.idItemCategory = idItemCategory;
    }

    public int getIdWishlist() {
        return idWishlist;
    }

    public void setIdWishlist(int idWishlist) {
        this.idWishlist = idWishlist;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WishlistItem{" +
                "idWishlistItem=" + idWishlistItem +
                ", nameWishlistItem='" + nameWishlistItem + '\'' +
                ", price=" + price +
                ", monthlyPay=" + monthlyPay +
                ", creationDate=" + creationDate +
                ", endDate=" + endDate +
                ", priority=" + priority +
                ", status=" + status +
                ", idItemCategory=" + idItemCategory +
                ", idWishlist=" + idWishlist +
                '}';
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
