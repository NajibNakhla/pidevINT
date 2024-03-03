package edu.esprit.pi.tests;

import edu.esprit.pi.entities.SubCategory;
import edu.esprit.pi.services.SubCategoryService;

import java.util.List;
public class MainClass {
    public static void main(String[] args) {
//         MyConnection mc = new MyConnection();
        SubCategory c = new SubCategory("Rent",300,40,5);
        SubCategory c1 = new SubCategory("food",500,100,5);
        SubCategoryService ps = new SubCategoryService();
     ps.addEntity(c);
      ps.addEntity(c1);
//
        System.out.println("Before updating:");
        System.out.println(ps.getAllData());

        // after updating
        ps.updateEntity("UpdatedOne",5);

        // Display data after update
        System.out.println("After updating:");
        System.out.println(ps.getAllData());
        SubCategoryService subCategoryService = new SubCategoryService();

        // Retrieve a subcategory to delete (assuming you have one in the database)
        List<SubCategory> subcategories = subCategoryService.getAllData();
        if (!subcategories.isEmpty()) {
            SubCategory subcategoryToDelete = subcategories.get(0); // Assuming you want to delete the first subcategory

            // Displaying subcategory details before deletion
            System.out.println("Subcategory before deletion:");
            System.out.println(subcategoryToDelete);

            // Deleting the subcategory
            int subcategoryIdToDelete = subcategoryToDelete.getId();
            subCategoryService.deleteEntityById(subcategoryIdToDelete);

            // Print a message indicating that the subcategory has been deleted
            System.out.println("Subcategory deleted successfully");
        } else {
            System.out.println("No subcategories found in the database.");
        }
}}
