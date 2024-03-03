package Entities;

import java.util.List;

public class Budget {
    private int idBudget;

    private double totalBudget;
    private double readyToAssign;

    private int idWallet;

    private List<Category> categories;

    public Budget(){
        totalBudget=Account.getTotalBalance();
        readyToAssign=0.0;
    }

    public int getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(int idBudget) {
        this.idBudget = idBudget;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public double getReadyToAssign() {
        return readyToAssign;
    }

    public void setReadyToAssign(double readyToAssign) {
        this.readyToAssign = readyToAssign;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "idBudget=" + idBudget +
                ", totalBudget=" + totalBudget +
                ", readyToAssign=" + readyToAssign +
                ", categories=" + categories +
                '}';
    }
}
