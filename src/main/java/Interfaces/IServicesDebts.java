package Interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IServicesDebts<T>{
    void addEntity(T t) throws SQLException;
    void updateEntity(T t) throws SQLException;
    void deleteEntity(T t);
    List<T> getAllData();
}