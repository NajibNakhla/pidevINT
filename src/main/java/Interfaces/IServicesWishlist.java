package Interfaces;

import java.util.List;

public interface IServicesWishlist<T>{
    void addEntity(T t);
    void updateEntity(T t, int id);
    void deleteEntity(int id);
    List<T> getAllData();
}
