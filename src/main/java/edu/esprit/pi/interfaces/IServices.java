package edu.esprit.pi.interfaces;


import java.util.List;

public interface IServices<T>{
    void addEntity(T t);
    void updateEntity(String newName, int id);
//    void deleteEntity(T t);
    void deleteEntityById(int id);
     List<T> getAllData();
}
