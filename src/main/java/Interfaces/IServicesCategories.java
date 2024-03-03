package Interfaces;


import java.util.List;

public interface IServicesCategories<T>{
    void addEntity(T t);
    void updateEntity(String newName, int id);
//    void deleteEntity(T t);
    void deleteEntityById(int id);
     List<T> getAllData();
}
