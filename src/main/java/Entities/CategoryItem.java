package Entities;

public class CategoryItem {
    private int idItemCategory;
    private String nameItemCategory;


    public CategoryItem() {
    }

    public CategoryItem(String nameItemCategory) {
        this.nameItemCategory = nameItemCategory;
    }

    public int getIdItemCategory() {
        return idItemCategory;
    }

    public void setIdItemCategory(int idItemCategory) {
        this.idItemCategory = idItemCategory;
    }

    public String getNameItemCategory() {
        return nameItemCategory;
    }

    public void setNameItemCategory(String nameItemCategory) {
        this.nameItemCategory = nameItemCategory;
    }

    @Override
    public String toString() {
        return "CategoryItem{" +
                "idItemCategory=" + idItemCategory +
                ", nameItemCategory='" + nameItemCategory + '\'' +
                '}';
    }
}
