package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wallet {
    private int idWallet ;
    private String walletName;
    private String walletCurrency;
    private int idUser;

    List<Account> accounts;

    public Wallet(int idWallet, String walletName, String walletCurrency, int idUser) {
        this.idWallet = idWallet;
        this.walletName = walletName;
        this.walletCurrency = walletCurrency;
        this.idUser = idUser;
        accounts = new ArrayList<>();
    }

    public Wallet(String walletName, String walletCurrency, int idUser) {
        this.walletName = walletName;
        this.walletCurrency = walletCurrency;
        this.idUser = idUser;
        accounts = new ArrayList<>();
    }

    public int getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletCurrency() {
        return walletCurrency;
    }

    public void setWalletCurrency(String walletCurrency) {
        this.walletCurrency = walletCurrency;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return idWallet == wallet.idWallet && idUser == wallet.idUser && Objects.equals(walletName, wallet.walletName) && Objects.equals(walletCurrency, wallet.walletCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idWallet, walletName, walletCurrency, idUser);
    }
}
