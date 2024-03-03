package Entities;

public class User {
    private int idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private IncomeType incomeType;
    private BudgetingType budgetType;
    private boolean rent;
    private boolean debt;
    private Transport transport;
    private String urlImage;

    public User() {}

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(int idUser, String firstName, String lastName, String email, String password) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(int idUser, String firstName, String lastName, String email, String password, String role, IncomeType incomeType, BudgetingType budgetType, boolean rent, boolean debt, Transport transport) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.incomeType = incomeType;
        this.budgetType = budgetType;
        this.rent = rent;
        this.debt = debt;
        this.transport = transport;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    public BudgetingType getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetingType budgetType) {
        this.budgetType = budgetType;
    }

    public boolean isRent() {
        return rent;
    }

    public void setRent(boolean rent) {
        this.rent = rent;
    }

    public boolean isDebt() {
        return debt;
    }

    public void setDebt(boolean debt) {
        this.debt = debt;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    /*@Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }*/

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "********"+ '\'' +
                ", role='" + role + '\'' +
                ", incomeType=" + incomeType +
                ", budgetType=" + budgetType +
                ", rent=" + rent +
                ", debt=" + debt +
                ", transport=" + transport +
                '}';
    }
}
