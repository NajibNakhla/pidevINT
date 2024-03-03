package edu.esprit.pi.entities;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private double budgetLimit;
    private List<SubCategory> subCategories;

    public Category() {
        subCategories = new ArrayList<>();
    }

    public Category(String name, double budgetLimit) {
        this.name = name;
        this.budgetLimit = budgetLimit;
        this.subCategories = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public void addSubCategory(SubCategory subCategory) {
        subCategories.add(subCategory);
    }

    public void removeSubCategory(SubCategory subCategory) {
        subCategories.remove(subCategory);
    }





    @Override
    public String toString() {
        return

                " Name='" + name + '\'' +
                ", Your budget limit=" + budgetLimit ;
    }

}
