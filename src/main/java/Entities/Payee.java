package Entities;

import java.util.Objects;

public class Payee {
    private int idPayee;
    private String namePayee;

    public Payee() {}
    public Payee(String namePayee) {
        this.namePayee = namePayee;
    }

    public int getidPayee() {
        return idPayee;
    }

    public void setidPayee(int idPayee) {
        this.idPayee = idPayee;
    }

    public String getNamePayee() {
        return namePayee;
    }

    public void setNamePayee(String namePayee) {
        this.namePayee = namePayee;
    }

    @Override
    public String toString() {
        return "Payee{" +
                "idPayee=" + idPayee +
                ", namePayee='" + namePayee + '\'' +
                '}';
    }

    // Equality Comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Payee payee = (Payee) obj;
        return idPayee == payee.idPayee && namePayee.equals(payee.namePayee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namePayee, namePayee);
    }

    // Serialization Methods (assuming using JSON for simplicity)
    public String toJson() {
        return "{\"payeeID\":" + idPayee + ",\"namePayee\":\"" + namePayee + "\"}";
    }

    /*public static Payee fromJson(String json) {
        // Parse JSON and create a new Payee object
        // (Note: This is a simple example; you might want to use a library like Jackson for robust JSON parsing)
        return new Payee(1, "Example Payee");
    } */
}
