package Interfaces;

import java.util.List;

public interface ITransaction <T>{


    void addTransactionService(T t);
    void updateEntity();
    void deleteEntity(T t);
    void deleteEntityByID(int id);
    List<T> getAllData();
    List<T> getAllTransactions();

}
