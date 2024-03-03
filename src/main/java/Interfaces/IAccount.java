package Interfaces;

import java.util.List;

public interface IAccount<T> {

    void addEntity(T t);
    void updateEntity(String newNameAcc,double newBalance,String newDescription, int id);
    void deleteEntity(T t);
    void deleteEntityByID(int id);
    List<T> getAllData();
    List<T> getAllTransactions(int id);
}
