package Interfaces;

import Entities.BudgetingType;
import Entities.IncomeType;
import Entities.Transport;
import Entities.User;

import java.util.Optional;

public interface IUserService{
    void addTransport(Transport transport, int id);
    void addRent(Boolean bool, int id);
    void addIncome (IncomeType incomeType, int id);
    void addBudgeting (BudgetingType budgetingType,int id);
    User login(String email, String password);

    User selectUserById(int id);
    void register(User user);
    boolean emailExists(String email);
    void addDebt(Boolean bool,int id);





}
