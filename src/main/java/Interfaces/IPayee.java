package Interfaces;
import java.util.List;
public interface IPayee <T>{
    void addEntity(T t);
    void updateEntity(T t, int id);
    void deleteEntity(int id);
    List<T> getAllData();

}
