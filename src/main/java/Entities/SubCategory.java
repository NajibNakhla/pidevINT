package Entities;

public class SubCategory {
    private int id;
    private String nom;
    private double MtAssigné;
    private double MtDépensé;
    private int idCategory; // Add idCategory attribute

    public SubCategory() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getMtAssigné() {
        return MtAssigné;
    }

    public void setMtAssigné(double mtAssigné) {
        MtAssigné = mtAssigné;
    }

    public double getMtDépensé() {
        return MtDépensé;
    }

    public void setMtDépensé(double mtDépensé) {
        MtDépensé = mtDépensé;
    }

    public int getIdCategory() { // Add getter method for idCategory
        return idCategory;
    }

    public void setIdCategory(int idCategory) { // Add setter method for idCategory
        this.idCategory = idCategory;
    }

    public SubCategory(int id, String nom, double MtAssigné, double MtDépensé, int idCategory) {
        this.id = id;
        this.nom = nom;
        this.MtAssigné = MtAssigné;
        this.MtDépensé = MtDépensé;
        this.idCategory = idCategory; // Update constructor to accept idCategory
    }

    @Override
    public String toString() {
        return String.format("Nom: %s   Montant Assigné: %.2f   Montant Dépensé: %.2f", nom, MtAssigné, MtDépensé);
    }


    public SubCategory(String nom, double MtAssigné, double MtDépensé, int idCategory) {
        this.nom = nom;
        this.MtAssigné = MtAssigné;
        this.MtDépensé = MtDépensé;
        this.idCategory = idCategory; // Update constructor to accept idCategory
    }
}
