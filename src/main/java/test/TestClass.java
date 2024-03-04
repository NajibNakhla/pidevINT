package test;


import Entities.Account;
import Entities.Payee;
import Entities.Transaction;
import enums.AccountType;
import enums.TransactionType;
import Services.*;

import java.time.LocalDate;

public class TestClass {
    public static void main(String[] args) {

     PayeeService ps = new PayeeService();

        //testing payee methods
    System.out.println("***************  Testing Payee Methods ******************");
       Payee payee1 = new Payee("Jumia");
       Payee payee2 = new Payee("Najib");
        Payee payee3 = new Payee("Rabie");
        Payee payee4 = new Payee("Eya");

      // ps.addEntity(payee2);
      //   ps.addEntity(payee3);
    //  ps.addEntity(payee1);
     //  ps.addEntity(payee4);
        //  ps.updateEntity(payee4,4);
       //ps.deleteEntity(13);
       // System.out.println(ps.getAllData());

        System.out.println(ps.getAllData());

       System.out.println("***************  Testing Account Methods ******************");
       AccountService as = new AccountService();
       Account account1 = new Account("cash account",AccountType.CASH,100.5,"notes for account 1");
       Account account2 = new Account("savings account",AccountType.SAVINGS,100.5,"notes for account 2");
       Account account3 = new Account("saving account2",AccountType.SAVINGS,300,"notes for account 3");
       Account account4 = new Account("saving account3",AccountType.SAVINGS,200,"notes for account 3");
     //  as.addEntity(account1);
      // as.addEntity(account2);
      //  as.addEntity(account3);
     //   as.addEntity(account4);


         //as.updateEntity("updated",50000.62,"updated note",3);
       // System.out.println(Account.getTotalBalance());


       // as.deleteEntityByID(1);
       // as.deleteEntityByID(2);
        // as.deleteEntityByID(7);
        //as.updateEntity("updating test",1000,"updated",6);
        System.out.println("number of accounts are " +as.countAccountsForWallet(1));


        System.out.println("testing budget creation , and that the attributes are intialized from Account data using an empty constructor ");
      //  Budget TheOnlyBudget = new Budget();
      //  BudgetService bs = new BudgetService();
       // bs.addEntity(TheOnlyBudget);
     //   System.out.println(bs.getData()); //Success , this is the Main Budget of the whole project .


        System.out.println("testing Wallet Services ");
        WalletService ws = new WalletService();
        System.out.println(ws.getTotalBalanceData(1));

        System.out.println("testing Transactions  ");
        TransactionService ts = new TransactionService();
        System.out.println("accounts balance : "+ts.getAccountBalance(11));
       // ts.updateAccountBalance(11,220);
        // ws.updateTotalBalance(2000);

        Transaction transactionIncome1 = new Transaction(LocalDate.now(), TransactionType.INCOME,"salary2",1000.0,5,5   ,1,5);
        Transaction transactionIncome2 = new Transaction(LocalDate.now(), TransactionType.INCOME,"salary3",1000.0,5,5   ,1,5);
        Transaction transactionIncome5 = new Transaction(LocalDate.now(), TransactionType.INCOME,"salary5",1000.0,5,5   ,1,5);
        Transaction transactionIncome8 = new Transaction(LocalDate.now(), TransactionType.INCOME,"salary5",100000.0,5,5   ,1,5);
     //   ts.addIncomeTransaction(transactionIncome1);
     //   ts.addIncomeTransaction(transactionIncome2);
       // ts.addIncomeTransaction(transactionIncome8);

       // System.out.println(ts.getTransactionsByAccountId(5));

        //  System.out.println(as.getAllAccounts());
       // System.out.println(ts.getTransactionsDetailsForAccount(4));
        System.out.println(ps.getPayeeNames());
        System.out.println(ps.getPayeeIdByName("Najib"));


        System.out.println(as.getCurrentBalance(4));


        System.out.println(as.getAccountIdByName("e dinar "));
        ////System.out.println(as.getAccountsNames());

        System.out.println(ts.getTransactionById(63));
       // ts.deleteTransactionById(67);

       // as.deleteEntityByID(20);
        //System.out.println(as.getAllAccounts());

      //  as.updateAccount(26,"najib4",500,"test");

        System.out.println(as.getCategoryNames());
        System.out.println(as.getCategoryIdByName("Needs"));


        Account account11 = new Account("testing services",AccountType.SAVINGS,100000,"notes for account 3");
        //  as.addEntity(account1);
     //   as.addEntity(account11);
      //      System.out.println(as.getAllAccounts());


        System.out.println("*************Testing API for exchange Rate ***************");
            // Arrange
            CurrencyService currencyService = new CurrencyService();

            // Act




        double exchangeRate = currencyService.getExchangeRate("USD", "TND");

        // Print the result
      //    System.out.println("Exchange rate from USD to TND: " + exchangeRate);


      //  ws.updateWalletName(1,"najib Wallet");

      //  ws.updateWalletCurrencyAndBalance(1,"TND",currencyService);
      //  System.out.println(ws.getCurrency(1));
       // System.out.println(ts.getAllTransactions());

        System.out.println(as.getAccountNameById(42));




    }
}

